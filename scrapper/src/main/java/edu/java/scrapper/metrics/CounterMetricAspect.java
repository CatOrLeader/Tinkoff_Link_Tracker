package edu.java.scrapper.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.constraints.NotNull;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(prefix = "app", name = "metrics.counter-enabled", havingValue = "true")
public class CounterMetricAspect {
    private final Counter counter;

    public CounterMetricAspect(@NotNull MeterRegistry meterRegistry) {
        this.counter = meterRegistry.counter("proceeded_messages");
    }

    @Pointcut("execution (public * edu.java.scrapper.rest.api.*Controller.*(..))")
    private void count() {
    }

    @AfterReturning(value = "count()")
    public void countBefore() {
        counter.increment();
    }
}
