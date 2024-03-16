package edu.java.scrapper.scheduler;

public sealed interface UpdateScheduler permits LinkUpdateScheduler {
    void update();
}
