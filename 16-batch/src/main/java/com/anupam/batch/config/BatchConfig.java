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

/**
 * Spring Batch configuration for the transaction import job.
 * <p>
 * Defines the complete batch pipeline: reader (CSV file), processor (validation
 * and suspicious-flag logic), and writer (JDBC insert). Also configures fault
 * tolerance with skip and retry policies.
 * </p>
 *
 * @author Anupam
 */
@Configuration
public class BatchConfig {

    /**
     * Configures a flat-file reader that parses transactions from a CSV resource.
     * <p>
     * The reader skips the header line and maps each row into a {@link Transaction} record.
     * </p>
     *
     * @return a configured {@link FlatFileItemReader} for transactions
     */
    @Bean
    public FlatFileItemReader<Transaction> reader() {
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("transactionReader")
                // Read from the CSV file on the classpath
                .resource(new ClassPathResource("sample-transactions.csv"))
                // Skip the CSV header row
                .linesToSkip(1)
                .delimited()
                .names("id", "accountId", "amount", "type", "timestamp")
                // Map each CSV row to a Transaction record
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

    /**
     * Creates the transaction processor bean responsible for validation
     * and suspicious-activity flagging.
     *
     * @return a new {@link TransactionProcessor} instance
     */
    @Bean
    public TransactionProcessor processor() {
        return new TransactionProcessor();
    }

    /**
     * Configures a JDBC batch writer that inserts processed transactions into the database.
     *
     * @param dataSource the data source used for database connections
     * @return a configured {@link JdbcBatchItemWriter} for transactions
     */
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

    /**
     * Defines the batch step that orchestrates reading, processing, and writing transactions.
     * <p>
     * Configured with chunk-based processing (100 items per chunk) and fault tolerance:
     * up to 10 skipped exceptions and 3 retries per item.
     * </p>
     *
     * @param jobRepository       the repository for persisting job metadata
     * @param transactionManager  the platform transaction manager for chunk transactions
     * @param reader              the item reader for CSV input
     * @param processor           the item processor for validation and flagging
     * @param writer              the item writer for database output
     * @return a configured {@link Step} for the import process
     */
    @Bean
    public Step importStep(JobRepository jobRepository,
                           PlatformTransactionManager transactionManager,
                           FlatFileItemReader<Transaction> reader,
                           TransactionProcessor processor,
                           JdbcBatchItemWriter<Transaction> writer) {
        return new StepBuilder("importTransactions", jobRepository)
                // Process 100 transactions per chunk
                .<Transaction, Transaction>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                // Enable fault tolerance with skip and retry policies
                .faultTolerant()
                .skipLimit(10)
                .skip(Exception.class)
                .retryLimit(3)
                .retry(Exception.class)
                .build();
    }

    /**
     * Defines the batch job that executes the transaction import step.
     *
     * @param jobRepository the repository for persisting job metadata
     * @param importStep    the step to execute within this job
     * @return a configured {@link Job} for importing transactions
     */
    @Bean
    public Job importJob(JobRepository jobRepository, Step importStep) {
        return new JobBuilder("importTransactionsJob", jobRepository)
                .start(importStep)
                .build();
    }
}
