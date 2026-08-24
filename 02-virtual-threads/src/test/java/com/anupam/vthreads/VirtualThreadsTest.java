package com.anupam.vthreads;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VirtualThreadsTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldUseVirtualThreadsWhenConfigIsActive() {
        Map<String, Object> response = restTemplate.getForObject(
                "http://localhost:" + port + "/api/thread-info", Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("isVirtual")).isEqualTo(true);
    }

    @Test
    void shouldCompleteBlockingEndpoint() {
        Map<String, Object> response = restTemplate.getForObject(
                "http://localhost:" + port + "/api/blocking", Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("virtual")).isEqualTo(true);
        assertThat(response.get("durationMs")).isNotNull();
    }

    @Test
    void shouldRunParallelTasksConcurrently() {
        Map<String, Object> response = restTemplate.getForObject(
                "http://localhost:" + port + "/api/parallel", Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("totalTasks")).isEqualTo(10);
        assertThat(response.get("handlerIsVirtual")).isEqualTo(true);
        // Parallel execution of 10x500ms tasks should complete well under 5000ms
        assertThat((Integer) response.get("totalDurationMs")).isLessThan(3000);
    }

    @Test
    void shouldVerifyVirtualThreadExecutorWorks() {
        // Verify that virtual threads are available in the JVM
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var future = executor.submit(() -> Thread.currentThread().isVirtual());
            assertThat(future.get()).isTrue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
