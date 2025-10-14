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

    @GetMapping("/data")
    public String getData() {
        logger.info("Request received at /data endpoint");

        // Increment metric
        try {
            requestCounter.add(1);
        } catch (Exception e) {
            logger.warn("Metric increment failed (metrics not configured): {}", e.getMessage());
        }

        Span slowSpan = tracer.spanBuilder("slow-operation").startSpan();
        try {
            logger.info("Starting slow operation...");
            Thread.sleep(2000); // simulate slow work
            logger.info("Slow operation completed");
        } catch (InterruptedException e) {
            logger.error("Error during slow operation", e);
            Thread.currentThread().interrupt();
        } finally {
            slowSpan.end();
        }

        logger.info("Returning response from /data");
        return "Processed data successfully!";
    }
}
