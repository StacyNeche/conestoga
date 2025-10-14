package com.example.demo;

import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        // Initialize OpenTelemetry using the autoconfigure helper.
        // This will look for otel-config.yaml on the classpath (resources).
        AutoConfiguredOpenTelemetrySdk.initialize();
        SpringApplication.run(DemoApplication.class, args);
    }
}
