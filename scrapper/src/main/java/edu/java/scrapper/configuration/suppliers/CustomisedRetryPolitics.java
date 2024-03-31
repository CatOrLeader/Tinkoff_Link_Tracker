package edu.java.scrapper.configuration.suppliers;

import java.util.Set;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.web.client.HttpClientErrorException;

public class CustomisedRetryPolitics extends SimpleRetryPolicy {
    private final Set<Integer> codesToRetry;

    public CustomisedRetryPolitics(int maxAttempts, Set<Integer> codesToRetry) {
        super(maxAttempts);
        this.codesToRetry = codesToRetry;
    }

    @Override
    public boolean canRetry(RetryContext context) {
        if (context.getLastThrowable() instanceof HttpClientErrorException exception) {
            return codesToRetry.contains(exception.getStatusCode().value());
        }

        return super.canRetry(context);
    }
}
