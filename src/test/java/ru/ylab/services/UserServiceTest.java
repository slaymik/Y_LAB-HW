package ru.ylab.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ylab.model.User;
import ru.ylab.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }
    @AfterEach
    void clear(){
        UserRepository userRepository = new UserRepository();
        userRepository.clear();
    }

    @Test
    void testRegisterUser() {
        User user = userService.registerUser("test@example.com", "password123", "Test User");
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testLoginUser() {
        userService.registerUser("test@example.com", "password123", "Test User");
        User loggedInUser = userService.loginUser("test@example.com", "password123");
        assertNotNull(loggedInUser);
        assertEquals("test@example.com", loggedInUser.getEmail());
    }

    @Test
    void testEditUser() {
        userService.registerUser("test@example.com", "password123", "Test User");
        userService.editUser("test@example.com", "newemail@example.com", "New Name", "newpassword123");
        User editedUser = userService.getUser("newemail@example.com");
        assertEquals("New Name", editedUser.getName());
        assertEquals("newemail@example.com", editedUser.getEmail());
    }

    @Test
    void testDeleteUser() {
        userService.registerUser("test@example.com", "password123", "Test User");
        userService.deleteUser("test@example.com");
        assertThrows(IllegalArgumentException.class, () ->
                userService.getUser("test@example.com"));
    }

    @Test
    void testResetPassword() {
        userService.registerUser("test@example.com", "password123", "Test User");
        userService.resetPassword("test@example.com", "password123", "newpassword123");
        User user = userService .getUser("test@example.com");
        assertEquals("newpassword123", user.getPassword());
    }

    @Test
    void testGetUser() {
        userService.registerUser("test@example.com", "password123", "Test User");
        User user = userService.getUser("test@example.com");
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }
}