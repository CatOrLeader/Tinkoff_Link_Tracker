package edu.java.bot.configuration;

import edu.java.bot.configuration.suppliers.CustomisedRetryPolitics;
import edu.java.bot.configuration.suppliers.LinearBackOffPolicy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryPoliticsConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "retry-politics.type", havingValue = "constant")
    public RetryTemplate constantRetryTemplate(ApplicationConfig config) {
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

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "retry-politics.type", havingValue = "linear")
    public RetryTemplate linearRetryTemplate(ApplicationConfig config) {
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

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "retry-politics.type", havingValue = "exponential")
    public RetryTemplate exponentialRetryTemplate(ApplicationConfig config) {
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
