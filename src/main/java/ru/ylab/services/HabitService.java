package ru.ylab.services;

import ru.ylab.model.Habit;
import ru.ylab.model.HabitAction;
import ru.ylab.repository.HabitRepository;
import ru.ylab.utils.MessageFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HabitService {

    private final HabitRepository habitRepository = new HabitRepository();
    private final UserService userService = new UserService();

    public void createHabit(String email, Habit habitData) {
        habitRepository.createHabit(userService.getUser(email), habitData);
    }

    public void updateHabit(Habit habitData) {
        habitRepository.updateHabit(habitData);
    }

    public void deleteHabit(int id) {
        habitRepository.deleteHabit(id);
    }

    public void executeHabit(int id, LocalDate date) {
        Habit habit = habitRepository.getHabit(id);
        habit.getActions().stream()
                .filter(habitAction -> habitAction.getActionDate().isEqual(date))
                .findFirst().orElseThrow().setCompleted(true);
        habit.setStreak(habit.getStreak() + 1);
    }

    public String getHabitHistory(int id) {
        Habit habit = habitRepository.getHabit(id);
        return MessageFactory.habitHistoryMessage(habit);
    }

    public String generateStatisticForPeriod(int id, LocalDate from, LocalDate to, String period) {
        Habit habit = habitRepository.getHabit(id);
        List<HabitAction> actions = getSuccessfulActionsForPeriod(id, from, to);
        return MessageFactory.statisticForPeriodMessage(period, habit, actions);
    }

    public String getFilteredHabitAction(int id, String filter) {
        List<HabitAction> actions = habitRepository.getHabit(id).getActions();
        return MessageFactory.filteredHabitActionsMessage(filter, actions);
    }

    public List<HabitAction> getSuccessfulActionsForPeriod(int id, LocalDate from, LocalDate to) {
        return habitRepository.getHabit(id).getActions().parallelStream()
                .filter(action -> action.getActionDate().isAfter(from) && action.getActionDate().isBefore(to))
                .collect(Collectors.toList());
    }
}