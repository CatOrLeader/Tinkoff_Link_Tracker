package edu.java.scheduler;

import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
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
