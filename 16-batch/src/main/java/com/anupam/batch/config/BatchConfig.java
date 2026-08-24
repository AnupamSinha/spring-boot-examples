package com.anupam.batch.config;

import com.anupam.batch.model.Transaction;
import com.anupam.batch.processor.TransactionProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class BatchConfig {

    @Bean
    public FlatFileItemReader<Transaction> reader() {
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("transactionReader")
                .resource(new ClassPathResource("sample-transactions.csv"))
                .linesToSkip(1)
                .delimited()
                .names("id", "accountId", "amount", "type", "timestamp")
                .fieldSetMapper(fieldSet -> new Transaction(
                        fieldSet.readLong("id"),
                        fieldSet.readString("accountId"),
                        new BigDecimal(fieldSet.readString("amount")),
                        fieldSet.readString("type"),
                        LocalDateTime.parse(fieldSet.readString("timestamp")),
                        false
                ))
                .build();
    }

    @Bean
    public TransactionProcessor processor() {
        return new TransactionProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Transaction> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .sql("""
                    INSERT INTO transactions (id, account_id, amount, type, timestamp, suspicious)
                    VALUES (:id, :accountId, :amount, :type, :timestamp, :suspicious)
                    """)
                .beanMapped()
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step importStep(JobRepository jobRepository,
                           PlatformTransactionManager transactionManager,
                           FlatFileItemReader<Transaction> reader,
                           TransactionProcessor processor,
                           JdbcBatchItemWriter<Transaction> writer) {
        return new StepBuilder("importTransactions", jobRepository)
                .<Transaction, Transaction>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipLimit(10)
                .skip(Exception.class)
                .retryLimit(3)
                .retry(Exception.class)
                .build();
    }

    @Bean
    public Job importJob(JobRepository jobRepository, Step importStep) {
        return new JobBuilder("importTransactionsJob", jobRepository)
                .start(importStep)
                .build();
    }
}
