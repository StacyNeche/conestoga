package com.example.demo.controller;

import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class DataController {

    private static final Logger logger = LoggerFactory.getLogger(DataController.class);
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("demo-app");
    private final Meter meter = GlobalOpenTelemetry.getMeter("demo-app");
    private final LongCounter requestCounter = meter
            .counterBuilder("data_requests_total")
            .setDescription("Counts total /data endpoint hits")
            .setUnit("1")
            .build();

    // In-memory counters for Task 2
    private final AtomicInteger inMemoryRequestCount = new AtomicInteger(0);
    private final AtomicInteger inMemoryFailedCount = new AtomicInteger(0);
    private final AtomicInteger totalResponseTimeMs = new AtomicInteger(0);

    @GetMapping("/data")
    public String getData() {
        long startTime = System.currentTimeMillis();
        inMemoryRequestCount.incrementAndGet(); // increment request counter
        logger.info("Request received at /data endpoint");

        // Increment OpenTelemetry metric (if configured)
        try {
            requestCounter.add(1);
        } catch (Exception e) {
            logger.warn("Metric increment failed (metrics not configured): {}", e.getMessage());
        }

        Span slowSpan = tracer.spanBuilder("slow-operation").startSpan();
        try {
            logger.info("Starting slow operation...");
            Thread.sleep(2000); // simulate slow work

            // Simulate occasional failure
            if (Math.random() < 0.2) {
                inMemoryFailedCount.incrementAndGet();
                throw new RuntimeException("Simulated failure");
            }

            logger.info("Slow operation completed");
            return "Processed data successfully!";
        } catch (Exception e) {
            inMemoryFailedCount.incrementAndGet();
            logger.error("Error during /data processing: {}", e.getMessage());
            return "Error processing data: " + e.getMessage();
        } finally {
            slowSpan.end();
            long duration = System.currentTimeMillis() - startTime;
            totalResponseTimeMs.addAndGet((int) duration);

            logger.info("Request #{} | Duration: {} ms | Failed: {}",
                    inMemoryRequestCount.get(),
                    duration,
                    inMemoryFailedCount.get());
        }
    }

    @GetMapping("/metrics")
    public String getMetrics() {
        int completedRequests = inMemoryRequestCount.get() - inMemoryFailedCount.get();
        double avgResponseTime = completedRequests > 0 ?
                ((double) totalResponseTimeMs.get() / completedRequests) : 0.0;

        return "Total Requests: " + inMemoryRequestCount.get() +
                " | Failed Requests: " + inMemoryFailedCount.get() +
                " | Average Response Time: " + avgResponseTime + " ms";
    }
}
