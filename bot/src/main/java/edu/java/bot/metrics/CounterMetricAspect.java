package edu.java.bot.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.constraints.NotNull;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    @Pointcut("execution (public * edu.java.bot.rest.api.*Controller.*(..))")
    private void count() {
    }

    @Before(value = "count()")
    public void countBefore() {
        counter.increment();
    }
}
