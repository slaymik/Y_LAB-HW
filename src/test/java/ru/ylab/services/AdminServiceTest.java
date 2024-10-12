package ru.ylab.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ylab.model.Admin;
import ru.ylab.model.Habit;
import ru.ylab.model.User;
import ru.ylab.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {
    private AdminService adminService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        adminService = new AdminService();
    }

    @AfterEach
    void clear(){
        userRepository.clear();
    }

    @Test
    void testLoginAsAdmin() {
        Admin admin = adminService.loginAsAdmin("admin", "password");
        assertNotNull(admin);
        assertEquals("admin", admin.getUsername());
    }

    @Test
    void testGetAllUsers() {
        userRepository.registerUser("test1@example.com", "password123", "Test User 1");
        userRepository.registerUser("test2@example.com", "password123", "Test User 2");
        String allUsers = adminService.getAllUsers();
        assertNotNull(allUsers);
    }

    @Test
    void testGetAllHabits() {
        User user = userRepository.registerUser("test@example.com", "password123", "Test User");
        Habit habitData = Habit.builder()
                .user(user)
                .name("Exercise")
                .description("Daily exercise")
                .frequency(Habit.Frequency.DAILY)
                .duration(30)
                .build();
        new HabitService().createHabit(habitData);
        String allHabits = adminService.getAllHabits(user.getEmail());
        assertNotNull(allHabits);
    }

    @Test
    void testDeleteUser() {
        User user = userRepository.registerUser("test@example.com", "password123", "Test User");
        adminService.deleteUser(user.getEmail());
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.getUser(user.getEmail()));
    }

    @Test
    void testBlockUser() {
        User user = userRepository.registerUser("test@example.com", "password123", "Test User");
        adminService.blockUser(user.getEmail());
        User blockedUser = userRepository.getUser(user.getEmail());
        assertTrue(blockedUser.isBlocked());
    }
}
