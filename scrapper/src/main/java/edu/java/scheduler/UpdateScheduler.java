package edu.java.scheduler;

public sealed interface UpdateScheduler permits LinkUpdateScheduler {
    void update();
}
