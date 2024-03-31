package edu.java.bot.configuration;

import edu.java.bot.configuration.suppliers.CustomisedRetryPolitics;
import edu.java.bot.configuration.suppliers.LinearBackOffPolicy;
import jakarta.xml.bind.ValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryPoliticsConfiguration {
    @Bean
    public RetryTemplate primaryRetryTemplate(ApplicationConfig config) throws ValidationException {
        return switch (config.retryPolitics().type()) {
            case CONSTANT -> constantRetryTemplate(config);
            case LINEAR -> linearRetryTemplate(config);
            case EXPONENTIAL -> exponentialRetryTemplate(config);
            case null -> throw new ValidationException("Incorrect type for the retry politics");
        };
    }

    private RetryTemplate constantRetryTemplate(ApplicationConfig config) {
        var politics = config.retryPolitics();
        RetryTemplate template = new RetryTemplate();

        template.setRetryPolicy(new CustomisedRetryPolitics(
            politics.maxAttempts(), politics.statusCodes()
        ));

        var policy = new FixedBackOffPolicy();
        policy.setBackOffPeriod(politics.config().constant().backOffPeriod());

        template.setBackOffPolicy(policy);

        return template;
    }

    private RetryTemplate linearRetryTemplate(ApplicationConfig config) {
        var politics = config.retryPolitics();
        RetryTemplate template = new RetryTemplate();

        template.setRetryPolicy(new CustomisedRetryPolitics(
            politics.maxAttempts(), politics.statusCodes()
        ));

        var policy = new LinearBackOffPolicy(
            politics.config().linear().initialInterval(),
            politics.config().linear().maxInterval()
        );

        template.setBackOffPolicy(policy);

        return template;
    }

    private RetryTemplate exponentialRetryTemplate(ApplicationConfig config) {
        var politics = config.retryPolitics();
        RetryTemplate template = new RetryTemplate();

        template.setRetryPolicy(new CustomisedRetryPolitics(
            politics.maxAttempts(), politics.statusCodes()
        ));

        var policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(politics.config().exponential().initialInterval());
        policy.setMultiplier(politics.config().exponential().mult());
        policy.setMaxInterval(politics.config().exponential().maxInterval());

        template.setBackOffPolicy(policy);

        return template;
    }
}

