# Monitoring Midterm - Java Starter Project (Spring Boot)

## Overview
This is a minimal Spring Boot application with a single endpoint `/data`.
It includes:
- Logging statements (INFO/WARN/ERROR)
- A simulated slow operation (Thread.sleep)
- Basic OpenTelemetry wiring (autoconfigure) and a pre-created counter metric

## How to run

1. Make sure you have Maven and Java 11+ installed.
2. Build and run:
   ```bash
   mvn spring-boot:run
   ```
3. Hit the endpoint:
   ```
   curl http://localhost:8080/data
   ```

## Notes for the instructor / environment
- This starter expects an OpenTelemetry collector available at `http://otel-collector:4317`.
  In the lab environment (e.g., Codespaces), either:
  - Run the collector in another container and network it as `otel-collector`, or
  - Adjust `src/main/resources/otel-config.yaml` to point to your OTLP endpoint.
- If you prefer using the OpenTelemetry Java agent instead of autoconfigure, you can run:
  ```bash
  java -javaagent:/path/opentelemetry-javaagent.jar -jar target/monitoring-midterm-0.0.1-SNAPSHOT.jar
  ```

## What students should do (exam)
- Verify logs at different levels appear.
- Confirm traces show up in SigNoz (or your APM) and check the `slow-operation` span.
- Confirm the `data_requests_total` metric increases when the endpoint is called.
