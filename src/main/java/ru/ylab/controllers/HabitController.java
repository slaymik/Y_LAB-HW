package ru.ylab.controllers;

import ru.ylab.io.TextInput;
import ru.ylab.io.TextOutput;
import ru.ylab.model.Habit;
import ru.ylab.model.User;
import ru.ylab.services.HabitService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class HabitController {
    private final HabitService habitService = new HabitService();

    public void habitMenu(User currentUser, TextOutput output, TextInput input) {
        while (true) {
            output.println("Меню привычек:");
            output.println("1. Создать привычку");
            output.println("2. Просмотреть привычки");
            output.println("3. Управление привычками");
            output.println("4. Статистика и аналитика");
            output.println("5. Выход в меню пользователя");
            int choice = input.nextInt();
            switch (choice) {
                case 1 -> createHabitMenu(currentUser, output, input);
                case 2 -> {
                    printAllHabits(currentUser, output);
                    output.println("");
                    habitMenu(currentUser, output, input);
                }
                case 3 -> habitTrackingMenu(currentUser, output, input);
                case 4 -> statisticsMenu(currentUser, output, input);
                case 5 -> {
                    return;
                }
                default -> {
                    output.println("Неправильный выбор. Пожалуйста, попробуйте еще раз.\n");
                    habitMenu(currentUser, output, input);
                }
            }
        }
    }

    public void createHabitMenu(User currentUser, TextOutput output, TextInput input) {
        output.println("");
        output.print("Введите название: ");
        String name = input.nextLine();
        output.print("Введите описание: ");
        String description = input.nextLine();
        output.print("Введите частоту (daily/weekly): ");
        String frequency = input.nextLine();
        output.print("Введите продолжительность (числом в днях): ");
        int duration = input.nextInt();

        habitService.createHabit(getHabitData(currentUser, name, description, frequency, duration));

        output.println("Привычка создана!\n");
        habitMenu(currentUser, output, input);
    }

    public void habitTrackingMenu(User currentUser, TextOutput output, TextInput input) {
        output.println("");
        output.println("1. Отметить выполнение привычки");
        output.println("2. Просмотреть историю выполнения");
        output.println("3. Посмотреть привычки с возможностью фильтрации по дате создания или статусу");
        output.println("4. Изменить информацию о привычке");
        output.println("5. Удалить привычку");
        output.println("6. Вернуться в Меню привычек");
        int choice = input.nextInt();
        switch (choice) {
            case 1 -> markHabitAsCompletedMenu(currentUser, output, input);
            case 2 -> viewHabitHistoryMenu(currentUser, output, input);
            case 3 -> viewFilteredHabitHistoryMenu(currentUser, output, input);

            case 4 -> editHabitMenu(currentUser, output, input);
            case 5 -> deleteHabitMenu(currentUser, output, input);
            case 6 -> habitMenu(currentUser, output, input);
            default -> {
                output.println("Неправильный выбор. Пожалуйста, попробуйте еще раз.\n");
                habitTrackingMenu(currentUser, output, input);
            }
        }
    }

    public void markHabitAsCompletedMenu(User currentUser, TextOutput output, TextInput input) {
        printAllHabits(currentUser, output);
        output.print("Введите id привычки: ");
        int id = input.nextInt();
        habitService.executeHabit(id, LocalDate.now());
        output.println("Привычка отмечена как выполненная!\n");
        habitMenu(currentUser, output, input);
    }

    public void viewHabitHistoryMenu(User currentUser, TextOutput output, TextInput input) {
        printAllHabits(currentUser, output);
        output.print("Введите id привычки: ");
        int id = input.nextInt();
        String history = habitService.getHabitHistory(id);
        output.println(history.concat("\n"));
        habitMenu(currentUser, output, input);
    }

    public void viewFilteredHabitHistoryMenu(User currentUser, TextOutput output, TextInput input) {
        output.println("Выберите фильтр: ");
        output.println("1. По статусу");
        output.println("2. По дате создания");
        int filter = input.nextInt();
        String email = currentUser.getEmail();

        switch (filter) {
            case 1 -> {
                output.println("Выберите статус:");
                output.println("1. Выполненные");
                output.println("2. Невыполненные");
                switch (input.nextInt()) {
                    case 1 -> output.println(habitService.getFilteredHabitActions(email, null, true));
                    case 2 -> output.println(habitService.getFilteredHabitActions(email, null, false));
                }
            }
            case 2 -> {
                output.print("Введите дату в формате (гггг-мм-дд): ");
                int dateInput = input.nextInt();
                LocalDate date = LocalDate.parse(Integer.toString(dateInput).trim().replace(" ", ""));
                output.println(habitService.getFilteredHabitActions(email, date, null));
            }
        }
    }

    public void editHabitMenu(User currentUser, TextOutput output, TextInput input) {
        printAllHabits(currentUser, output);
        output.print("Введите id привычки: ");
        int id = input.nextInt();
        Habit currentHabit = habitService.getHabit(id);
        output.print("Введите новое название[%s]: ".formatted(currentHabit.getName()));
        String name = input.nextLine();
        output.print("Введите новое описание[%s]: ".formatted(currentHabit.getDescription()));
        String description = input.nextLine();
        output.print("Введите новую частоту (daily/weekly)[%s]: ".formatted(currentHabit.getFrequency()));
        String frequency = input.nextLine();
        output.print("Введите новую продолжительность (числом в днях)[%s]: ".formatted(currentHabit.getDuration()));
        int duration = input.nextInt();

        Habit habitData = getHabitData(currentUser, name, description, frequency, duration);
        habitData.setId(id);
        habitService.editHabit(habitData);

        output.println("Привычка изменена!\n");
        habitMenu(currentUser, output, input);
    }

    public void deleteHabitMenu(User currentUser, TextOutput output, TextInput input) {
        printAllHabits(currentUser, output);
        output.print("Введите id привычки: ");
        int id = input.nextInt();

        habitService.deleteHabit(id);

        output.println("Привычка успешно удалена!\n");
        habitMenu(currentUser, output, input);
    }

    public void statisticsMenu(User currentUser, TextOutput output, TextInput input) {
        output.println("Статистика и аналитика:");
        output.println("1. Подсчет текущих серий выполнения привычек");
        output.println("2. Процент успешного выполнения привычек за определенный период");
        output.println("3. Формирование отчета для пользователя по прогрессу выполнения");
        output.println("4. Вернуться в Меню привычек");
        int choice = input.nextInt();
        switch (choice) {
            case 1 -> calculateStreakMenu(currentUser, output, input);
            case 2 -> calculateSuccessRateMenu(currentUser, output, input);

            case 3 -> generateReportMenu(currentUser, output, input);

            case 4 -> habitMenu(currentUser, output, input);

            default -> {
                output.println("Неправильный выбор. Пожалуйста, попробуйте еще раз.\n");
                statisticsMenu(currentUser, output, input);
            }
        }
    }

    public void calculateStreakMenu(User currentUser, TextOutput output, TextInput input) {
        List<Habit> habits = habitService.getAllHabits(currentUser);
        for (Habit habit : habits) {
            output.println("Привычка: " + habit.getName() + ", Серия: " + habit.getStreak());
        }
        output.println("");
        habitMenu(currentUser, output, input);
    }

    public void calculateSuccessRateMenu(User currentUser, TextOutput output, TextInput input) {
        printAllHabits(currentUser, output);
        output.print("Введите id привычки: ");
        int id = input.nextInt();
        output.print("Введите период (день/неделя/месяц): ");
        String period = input.nextLine();
        output.println("");
        String statistic = getStatistic(output, period, id);

        output.println(statistic.concat("\n"));
        habitMenu(currentUser, output, input);
    }

    public void generateReportMenu(User currentUser, TextOutput output, TextInput input) {
        List<Habit> habits = habitService.getAllHabits(currentUser);
        for (Habit habit : habits) {
            output.println("Привычка: " + habit.getName() + ", Прогресс: " + habit.getStreak());
        }
        output.println("");
        habitMenu(currentUser, output, input);
    }

    private static Habit getHabitData(User currentUser, String name, String description, String frequency, int duration) {
        if (Arrays.stream(Habit.Frequency.values()).noneMatch(fr -> fr.name().equalsIgnoreCase(frequency))) {
            throw new IllegalArgumentException("Неправильная частота - %s".formatted(frequency));
        }
        return Habit.builder()
                .user(currentUser)
                .name(name)
                .description(description)
                .frequency(Habit.Frequency.valueOf(frequency.toUpperCase()))
                .duration(duration)
                .build();
    }

    private void printAllHabits(User currentUser, TextOutput output) {
        List<Habit> habits = habitService.getAllHabits(currentUser);
        for (Habit habit : habits) {
            output.println(habit.toString());
        }
    }

    private String getStatistic(TextOutput output, String period, int id) {
        String statistic = "";
        switch (period.toLowerCase().trim()) {
            case "день" ->
                    statistic = habitService.generateStatisticForPeriod(id, LocalDate.now().minusDays(1), LocalDate.now(), period);

            case "неделя" ->
                    statistic = habitService.generateStatisticForPeriod(id, LocalDate.now().minusDays(7), LocalDate.now(), period);

            case "месяц" ->
                    statistic = habitService.generateStatisticForPeriod(id, LocalDate.now().minusDays(30), LocalDate.now(), period);

            default -> {
                output.println("Неправильный выбор. Пожалуйста, попробуйте еще раз.\n");
                getStatistic(output, period, id);
            }
        }
        return statistic;
    }
}