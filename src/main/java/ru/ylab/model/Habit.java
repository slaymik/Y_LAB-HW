package ru.ylab.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class Habit {
    private int id;
    private User user;
    private String name;
    private String description;
    private Frequency frequency;
    private int duration;

    @Builder.Default
    private int streak = 0;

    @Builder.Default
    private Instant createTimestamp = Instant.now();

    private List<HabitAction> actions;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return String.format("ID: %d, Пользователь: %s, Название: %s, Описание: %s, Частота: %s, Длительность: %d, Серия: %d, Дата создания: %s, Запланированное количество выполнений: %d",
                id, user, name, description, frequency, duration, streak, formatter.format(createTimestamp), actions.size());
    }

    public enum Frequency {
        DAILY,
        WEEKLY
    }
}