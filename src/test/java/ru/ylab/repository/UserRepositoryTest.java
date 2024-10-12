package ru.ylab.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ylab.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
    private static UserRepository userRepository;
    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    @AfterEach
    void clear(){
        userRepository.clear();
    }

    @Test
    void testRegisterUser_Success() {
        User user = userRepository.registerUser("test@example.com", "password123", "Test User");
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
    }

    @Test
    void testRegisterUser_DuplicateEmail() {
        userRepository.registerUser("test@example.com", "password123", "Test User");
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.registerUser("test@example.com", "password456", "Another User"));
    }

    @Test
    void testRegisterUser_InvalidEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.registerUser("invalidemail", "password123", "Test User"));
    }

    @Test
    void testRegisterUser_InvalidPassword() {
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.registerUser("test@example.com", "short", "Test User"));
    }

    @Test
    void testLoginUser_Success() {
        userRepository.registerUser("test@example.com", "password123", "Test User");
        User loggedInUser = userRepository.loginUser("test@example.com", "password123");
        assertNotNull(loggedInUser);
        assertEquals("test@example.com", loggedInUser.getEmail());
    }

    @Test
    void testLoginUser_InvalidEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.loginUser("nonexistent@example.com", "password123"));
    }

    @Test
    void testLoginUser_InvalidPassword() {
        userRepository.registerUser("test@example.com", "password123", "Test User");
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.loginUser("test@example.com", "wrongpassword"));
    }

    @Test
    void testLoginUser_BlockedUser() {
        User user = userRepository.registerUser("test@example.com", "password123", "Test User");
        user.setBlocked(true);
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.loginUser("test@example.com", "password123"));
    }

    @Test
    void testEditUser_Success() {
        userRepository.registerUser("test@example.com", "password123", "Test User");
        userRepository.editUser("test@example.com", "newemail@example.com", "New Name", "newpassword123");
        User editedUser = userRepository.getUser("newemail@example.com");
        assertEquals("New Name", editedUser.getName());
        assertEquals("newemail@example.com", editedUser.getEmail());
    }

    @Test
    void testEditUser_InvalidNewEmail() {
        userRepository.registerUser("test@example.com", "password123", "Test User");
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.editUser("test@example.com", "invalidemail", "New Name", "newpassword123"));
    }

    @Test
    void testEditUser_InvalidNewPassword() {
        userRepository.registerUser("test@example.com", "password123", "Test User");
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.editUser("test@example.com", "newemail@example.com", "New Name", "short")); }

    @Test
    void testGetUser_Success() {
        userRepository.registerUser("test@example.com", "password123", "Test User");
        User user = userRepository.getUser("test@example.com");
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testGetUser_NonexistentUser() {
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.getUser("nonexistent@example.com"));
    }

    @Test
    void testDeleteUser() {
        userRepository.registerUser("test@example.com", "password123", "Test User");
        userRepository.deleteUser("test@example.com");
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.getUser("test@example.com"));
    }

    @Test
    void testResetPassword_Success() {
        userRepository.registerUser("test@example.com", "password123", "Test User");
        userRepository.resetPassword("test@example.com", "password123", "newpassword123");
        User user = userRepository.getUser("test@example.com");
        assertEquals("newpassword123", user.getPassword());
    }

    @Test
    void testResetPassword_InvalidNewPassword() {
        userRepository.registerUser("test@example.com", "password123", "Test User");
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.resetPassword("test@example.com", "password123", "short"));
    }

    @Test
    void testGetAllUsers() {
        userRepository.registerUser("test1@example.com", "password123", "Test User 1");
        userRepository.registerUser("test2@example.com", "password123", "Test User 2");
        List<User> users = userRepository.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testBlockUser_Success() {
        userRepository.registerUser("test@example.com", "password123", "Test User");
        userRepository.blockUser("test@example.com");
        User user = userRepository.getUser("test@example.com");
        assertTrue(user.isBlocked());
    }

    @Test
    void testBlockUser_NonexistentUser() {
        assertThrows(IllegalArgumentException.class, () ->
                userRepository.blockUser("nonexistent@example.com"));
    }
}