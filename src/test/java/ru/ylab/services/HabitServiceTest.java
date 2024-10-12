package ru.ylab.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ylab.model.Habit;
import ru.ylab.model.HabitAction;
import ru.ylab.model.User;
import ru.ylab.repository.HabitRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HabitServiceTest {
    private HabitService habitService;
    private HabitRepository habitRepository;
    private User testUser;
    private Habit habitData;

    @BeforeEach
    void setUp() {
        habitRepository = new HabitRepository();
        testUser = new User("test@example.com", "password123", "Test User", false);
        habitService = new HabitService();
        habitData = Habit.builder()
                .user(testUser)
                .name("Exercise")
                .description("Daily exercise")
                .frequency(Habit.Frequency.DAILY)
                .duration(30)
                .build();
    }
    @AfterEach
    void clear(){
        habitRepository.clear();
    }

    @Test
    void testCreateHabit() {
        Habit habit = habitService.createHabit(habitData);
        assertNotNull(habit);
        assertEquals("Exercise", habit.getName());
        assertEquals(testUser, habit.getUser());
    }

    @Test
    void testEditHabit() {
        Habit habit = habitService.createHabit(habitData);
        Habit editedHabit = Habit.builder()
                .id(habit.getId())
                .user(testUser)
                .name("New Exercise")
                .description("New description")
                .frequency(Habit.Frequency.WEEKLY)
                .duration(60)
                .build();
        habitService.editHabit(editedHabit);
        assertEquals("New Exercise", editedHabit.getName());
        assertEquals(Habit.Frequency.WEEKLY, editedHabit.getFrequency());
    }

    @Test
    void testDeleteHabit() {
        Habit habit = habitService.createHabit(habitData);
        habitService.deleteHabit(habit.getId());
        assertThrows(IllegalArgumentException.class, () ->
                habitService.getHabit(habit.getId()));
    }

    @Test
    void testExecuteHabit_Daily() {
        Habit habit = habitService.createHabit(habitData);
        habitService.executeHabit(habit.getId(), LocalDate.now());
        Habit retrievedHabit = habitService.getHabit(habit.getId());
        assertEquals(1, retrievedHabit.getStreak());
    }

    @Test
    void testExecuteHabit_Weekly() {
        habitData.setFrequency(Habit.Frequency.WEEKLY);
        Habit habit = habitService.createHabit(habitData);
        habitService.executeHabit(habit.getId(), LocalDate.now());
        Habit retrievedHabit = habitService.getHabit(habit.getId());
        assertEquals(1, retrievedHabit.getStreak());
    }

    @Test
    void testGetHabitHistory() {
        Habit habit = habitService.createHabit(habitData);
        String habitHistory = habitService.getHabitHistory(habit.getId());
        assertNotNull(habitHistory);
    }

    @Test
    void testGetHabit() {
        Habit habit = habitService.createHabit(habitData);
        Habit retrievedHabit = habitService.getHabit(habit.getId());
        assertNotNull(retrievedHabit);
        assertEquals(habit.getId(), retrievedHabit.getId());
    }

    @Test
    void testGenerateStatisticForPeriod() {
        Habit habit = habitService.createHabit(habitData);
        String statistic = habitService.generateStatisticForPeriod(habit.getId(), LocalDate.now().minusDays(1), LocalDate.now(), "day");
        assertNotNull(statistic);
    }

    @Test
    void testGetFilteredHabitActions() {
        habitService.createHabit(habitData);
        String filteredActions = habitService.getFilteredHabitActions(testUser.getEmail(), LocalDate.now(), true);
        assertNotNull(filteredActions);
    }

    @Test
    void testGetAllHabits() {
        Habit habit1 = habitData;
        Habit habit2 = habitData;
        habit1.setName("Exercise 1");
        habit2.setName("Exercise 2");
        habitService.createHabit(habit1);
        habitService.createHabit(habit2);
        List<Habit> habits = habitService.getAllHabits(testUser);
        assertEquals(2, habits.size());
    }

    @Test
    void testGetSuccessfulActionsForPeriod() {
        Habit habit = habitService.createHabit(habitData);
        List<HabitAction> actions = habitService.getSuccessfulActionsForPeriod(habit.getId(), LocalDate.now().minusDays(1), LocalDate.now());
        assertNotNull(actions);
    }
}