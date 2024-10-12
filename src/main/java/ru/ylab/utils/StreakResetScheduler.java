package ru.ylab.utils;

import ru.ylab.repository.HabitRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StreakResetScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final HabitRepository repository = new HabitRepository();

    public void startScheduler() {
        scheduler.scheduleAtFixedRate(repository::resetStreaks, 0, 1, TimeUnit.DAYS);
    }
}