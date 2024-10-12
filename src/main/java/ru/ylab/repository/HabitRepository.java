package ru.ylab.repository;

import ru.ylab.model.Habit;
import ru.ylab.model.HabitAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.ylab.Consts.FrequencyConsts.DAYS_IN_WEEK;


public class HabitRepository {
    private static final Map<Integer, Habit> habits = new HashMap<>();
    private static int iterator = 1;

    public Habit createHabit(Habit habitData) {
        habitData = Habit.builder()
                .id(iterator)
                .user(habitData.getUser())
                .name(habitData.getName())
                .description(habitData.getDescription())
                .frequency(habitData.getFrequency())
                .duration(habitData.getDuration())
                .build();
        habitData.setActions(createHabitActionsBasedOnFrequency(habitData));
        habits.put(iterator, habitData);
        iterator++;
        return habitData;
    }

    public void editHabit(Habit habitData) {
        if (!habits.containsKey(habitData.getId())) {
            throw new IllegalArgumentException("Привычка с таким id не существует");
        }
        Habit habit = habits.get(habitData.getId());
        if (habit != null) {
            habit.setName(habitData.getName() != null && !habitData.getName().isBlank() ? habitData.getName() : habit.getName());
            habit.setDescription(habitData.getDescription() != null && !habitData.getDescription().isBlank() ? habitData.getDescription() : habit.getDescription());
            if (habit.getDuration() != habitData.getDuration() || habit.getFrequency() != habitData.getFrequency()) {
                habit.setFrequency(habitData.getFrequency());
                habit.setDuration(habitData.getDuration() != 0 ? habitData.getDuration() : habit.getDuration());
                habit.setActions(createHabitActionsBasedOnFrequency(habit));
                habit.setStreak(0);
            }
        }
    }

    public void deleteHabit(int id) {
        if (!habits.containsKey(id)) {
            throw new IllegalArgumentException("Привычка с таким id не существует");
        }
        habits.remove(id);
    }

    public Habit getHabit(int id) {
        if (!habits.containsKey(id)) {
            throw new IllegalArgumentException("Привычка с таким id не существует");
        }
        return habits.get(id);
    }

    public List<Habit> getAllHabits(String email) {
        return habits.values().stream().filter(habit -> habit.getUser().getEmail().equals(email)).toList();
    }

    public List<HabitAction> getAllHabitActions(String email) {
        return getAllHabits(email).stream()
                .flatMap(habit -> habit.getActions().stream())
                .toList();
    }

    public void resetStreaks() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        habits.values().stream()
                .filter(habit -> isHabitExpired(habit, yesterday))
                .forEach(habit -> habit.setStreak(0));
    }

    private static boolean isHabitExpired(Habit habit, LocalDate yesterday) {
        return habit.getActions()
                .stream()
                .anyMatch(habitAction -> !habitAction.isCompleted()
                                         && habitAction.getActionDate().isEqual(yesterday)
                );
    }

    private List<HabitAction> createHabitActionsBasedOnFrequency(Habit habit) {
        List<HabitAction> actions = new ArrayList<>();
        switch (habit.getFrequency()) {
            case DAILY -> {
                for (int i = 1; i <= habit.getDuration(); i++) {
                    actions.add(new HabitAction(habit.getId(), habit.getName(), LocalDate.now().plusDays((long) i - 1), false));
                }
            }
            case WEEKLY -> {
                for (int i = 1; i <= habit.getDuration() / DAYS_IN_WEEK; i++) {
                    actions.add(new HabitAction(habit.getId(), habit.getName(), LocalDate.now().plusDays((i - 1) * DAYS_IN_WEEK), false));
                }
            }
        }
        return actions;
    }
    public void clear() {
        habits.clear();
        iterator = 1;
    }
}