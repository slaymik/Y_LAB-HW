package ru.ylab.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.ylab.model.Habit;
import ru.ylab.model.HabitAction;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageFactory {

    public static String habitHistoryMessage(Habit habit) {
        StringBuilder builder = new StringBuilder();
        builder.append("Название: %s \n".formatted(habit.getName()));
        builder.append("Описание: %s \n".formatted(habit.getDescription()));
        builder.append("Частота: %s \n".formatted(habit.getFrequency()));
        builder.append("Текущая серия: %d \n".formatted(habit.getStreak()));

        List<HabitAction> actions = habit.getActions();
        if (actions.isEmpty()) {
            builder.append("Действий не найдено для данной привычки.");
        } else {
            for (int i = 0; i < actions.size(); i++) {
                builder.append("%d. ".formatted(i + 1)).append(actions.get(i).toString()).append("\n");
            }
        }

        return builder.toString();
    }

    public static String statisticForPeriodMessage(String period, Habit habit, List<HabitAction> actions) {
        StringBuilder builder = new StringBuilder();
        builder.append("Название: ").append(habit.getName()).append("\n");
        builder.append("Период: ").append(period).append("\n");

        int totalActions = actions.size();
        int completedActions = (int) actions.stream().filter(HabitAction::isCompleted).count();
        double completionRate = totalActions > 0 ? (double) completedActions / totalActions * 100 : 0;

        builder.append("Запланированное количество выполнений: ").append(totalActions).append("\n");
        builder.append("Реальное количество выполнений: ").append(completedActions).append("\n");
        builder.append(String.format("Процент успешного выполнения: %.2f%%", completionRate)).append("\n\n");

        for (int i = 0; i < actions.size(); i++) {
            builder.append("%d. ".formatted(i + 1)).append(actions.get(i).toString()).append("\n");
        }

        return builder.toString();
    }

    public static String filteredHabitActionsMessage(LocalDate filterDate, Boolean filterStatus, List<HabitAction> actions) {
        StringBuilder builder = new StringBuilder();

        if (filterDate != null && filterStatus != null) {
            actions.stream()
                    .filter(action -> action.getActionDate().equals(filterDate) && action.isCompleted() == filterStatus)
                    .forEach(action -> builder.append(action).append("\n"));
        } else if (filterDate != null) {
            actions.stream()
                    .filter(action -> action.getActionDate().equals(filterDate))
                    .forEach(action -> builder.append(action).append("\n"));
        } else if (filterStatus != null) {
            actions.stream()
                    .filter(action -> action.isCompleted() == filterStatus)
                    .forEach(action -> builder.append(action).append("\n"));
        }
        return builder.toString();
    }
}