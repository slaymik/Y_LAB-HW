package ru.ylab.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ylab.model.Habit;
import ru.ylab.model.HabitAction;
import ru.ylab.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HabitRepositoryTest {
    private HabitRepository habitRepository;
    private Habit habit;
    private User user;

    @BeforeEach
    public void setup() {
        habitRepository = new HabitRepository();
        user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        habit = new Habit();
        habit.setUser(user);
        habit.setName("Test Habit");
        habit.setDescription("Test Description");
        habit.setFrequency(Habit.Frequency.DAILY);
        habit.setDuration(7);
    }

    @AfterEach
    public void after(){
        habitRepository.clear();
    }

    @Test
    public void testCreateHabit() {
        habitRepository.createHabit(habit);
        Habit createdHabit = habitRepository.getHabit(1);
        assertEquals(habit.getName(), createdHabit.getName());
        assertEquals(habit.getDescription(), createdHabit.getDescription());
        assertEquals(habit.getFrequency(), createdHabit.getFrequency());
        assertEquals(habit.getDuration(), createdHabit.getDuration());
    }

    @Test
    public void testEditHabit() {
        habitRepository.createHabit(habit);
        Habit createdHabit = habitRepository.getHabit(1);

        createdHabit.setName("New Name");
        createdHabit.setDescription("New Description");
        createdHabit.setFrequency(Habit.Frequency.WEEKLY);
        createdHabit.setDuration(14);

        habitRepository.editHabit(createdHabit);

        Habit editedHabit = habitRepository.getHabit(1);

        assertEquals(createdHabit.getName(), editedHabit.getName());
        assertEquals(createdHabit.getDescription(), editedHabit.getDescription());
        assertEquals(createdHabit.getFrequency(), editedHabit.getFrequency());
        assertEquals(createdHabit.getDuration(), editedHabit.getDuration());
    }

    @Test
    public void testDeleteHabit() {
        habitRepository.createHabit(habit);
        habitRepository.deleteHabit(1);
        assertThrows(IllegalArgumentException.class, () -> habitRepository.getHabit(1));
    }

    @Test
    public void testGetHabit() {
        habitRepository.createHabit(habit);
        Habit retrievedHabit = habitRepository.getHabit(1);
        assertEquals(habit.getName(), retrievedHabit.getName());
        assertEquals(habit.getDescription(), retrievedHabit.getDescription());
        assertEquals(habit.getFrequency(), retrievedHabit.getFrequency());
        assertEquals(habit.getDuration(), retrievedHabit.getDuration());
    }

    @Test
    public void testGetAllHabits() {
        habitRepository.createHabit(habit);
        List<Habit> habits = habitRepository.getAllHabits(user.getEmail());
        assertEquals(1, habits.size());
        assertEquals(habit.getName(), habits.get(0).getName());
        assertEquals(habit.getDescription(), habits.get(0).getDescription());
        assertEquals(habit.getFrequency(), habits.get (0).getFrequency());
        assertEquals(habit.getDuration(), habits.get(0).getDuration());
    }

    @Test
    public void testGetAllHabitActions() {
        habitRepository.createHabit(habit);
        List<HabitAction> actions = habitRepository.getAllHabitActions(user.getEmail());
        assertEquals(7, actions.size());
        for (int i = 0; i < 7; i++) {
            assertEquals(LocalDate.now().plusDays(i), actions.get(i).getActionDate());
        }
    }

    @Test
    public void testResetStreaks() {
        habitRepository.createHabit(habit);
        habitRepository.resetStreaks();
        Habit habitAfterReset = habitRepository.getHabit(1);
        assertEquals(0, habitAfterReset.getStreak());
    }
}