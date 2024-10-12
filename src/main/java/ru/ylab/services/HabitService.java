package ru.ylab.services;

import ru.ylab.model.Habit;
import ru.ylab.model.HabitAction;
import ru.ylab.model.User;
import ru.ylab.repository.HabitRepository;
import ru.ylab.utils.MessageFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ylab.Consts.FrequencyConsts.DAYS_IN_WEEK;
public class HabitService {

    private final HabitRepository habitRepository = new HabitRepository();

    public Habit createHabit(Habit habitData) {
      return  habitRepository.createHabit(habitData);
    }

    public void editHabit(Habit habitData) {
        habitRepository.editHabit(habitData);
    }

    public void deleteHabit(int id) {
        habitRepository.deleteHabit(id);
    }

    public void executeHabit(int id, LocalDate date) {
        Habit habit = habitRepository.getHabit(id);
        switch (habit.getFrequency()) {
            case DAILY -> {
                habit.getActions().stream()
                        .filter(habitAction -> habitAction.getActionDate().isEqual(date))
                        .findFirst().orElseThrow().setCompleted(true);
                habit.setStreak(habit.getStreak() + 1);
            }
            case WEEKLY -> {
                habit.getActions().stream()
                        .filter(habitAction -> date.compareTo(habitAction.getActionDate()) <= DAYS_IN_WEEK)
                        .findFirst().orElseThrow().setCompleted(true);
                habit.setStreak(habit.getStreak() + 1);
            }
        }
    }

    public String getHabitHistory(int id) {
        Habit habit = habitRepository.getHabit(id);
        return MessageFactory.habitHistoryMessage(habit);
    }

    public Habit getHabit(int id) {
        return habitRepository.getHabit(id);
    }

    public String generateStatisticForPeriod(int id, LocalDate from, LocalDate to, String period) {
        Habit habit = habitRepository.getHabit(id);
        List<HabitAction> actions = getSuccessfulActionsForPeriod(id, from, to);
        return MessageFactory.statisticForPeriodMessage(period, habit, actions);
    }

    public String getFilteredHabitActions(String email, LocalDate filterDate, Boolean filterStatus) {
        List<HabitAction> actions = habitRepository.getAllHabitActions(email);
        return MessageFactory.filteredHabitActionsMessage(filterDate, filterStatus, actions);
    }

    public List<Habit> getAllHabits(User currentUser) {
        return habitRepository.getAllHabits(currentUser.getEmail());
    }

    public List<HabitAction> getSuccessfulActionsForPeriod(int id, LocalDate from, LocalDate to) {
        return habitRepository.getHabit(id).getActions().parallelStream()
                .filter(action -> action.getActionDate().isAfter(from) && action.getActionDate().isBefore(to))
                .collect(Collectors.toList());
    }
}