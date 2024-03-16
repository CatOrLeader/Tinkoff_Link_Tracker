package edu.java.scrapper.scheduler;

import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true")
@Service
public final class LinkUpdateScheduler implements UpdateScheduler {
    @Override
    @Scheduled(
        fixedRateString = "#{@scheduler.forceCheckDelay().toMillis()}"
    )
    public void update() {
        LogManager.getLogger().info("Logged on " + LocalDateTime.now());
    }
}

