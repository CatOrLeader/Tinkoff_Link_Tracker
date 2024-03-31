package edu.java.bot.configuration.suppliers;

import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.BackOffPolicy;

public class LinearBackOffPolicy implements BackOffPolicy {
    private final long initialInterval;
    private final long maxInterval;

    public LinearBackOffPolicy(long initialInterval, long maxInterval) {
        this.initialInterval = initialInterval;
        this.maxInterval = maxInterval;
    }

    @Override
    public BackOffContext start(RetryContext context) {
        return new LinearBackOffContext();
    }

    @Override
    public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
        try {
            Thread.sleep(
                Math.min(initialInterval * ++((LinearBackOffContext) backOffContext).attempts, maxInterval)
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class LinearBackOffContext implements BackOffContext {
        private int attempts;
    }
}
