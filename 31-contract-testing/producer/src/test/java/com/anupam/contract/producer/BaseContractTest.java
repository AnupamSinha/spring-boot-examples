package com.anupam.contract.producer;

import com.anupam.contract.producer.controller.PaymentController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

@SpringBootTest
public abstract class BaseContractTest {

    @Autowired
    private PaymentController paymentController;

    @BeforeEach
    void setup() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder =
                MockMvcBuilders.standaloneSetup(paymentController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
    }
}
