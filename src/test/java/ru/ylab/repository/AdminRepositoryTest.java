package ru.ylab.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ylab.model.Admin;

import static org.junit.jupiter.api.Assertions.*;

class AdminRepositoryTest {
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        adminRepository = new AdminRepository();
    }

    @Test
    void loginAsAdmin_validCredentials_shouldReturnAdmin() {
        Admin admin = adminRepository.loginAsAdmin("admin", "password");
        assertNotNull(admin);
        assertEquals("admin", admin.getUsername());
        assertEquals("password", admin.getPassword());
    }

    @Test
    void loginAsAdmin_invalidUsername_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> adminRepository.loginAsAdmin("invalid", "password"));

        assertEquals("Админа с таким username не существует", exception.getMessage());
    }

    @Test
    void loginAsAdmin_invalidPassword_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> adminRepository.loginAsAdmin("admin", "wrongpassword"));

        assertEquals("Неверный пароль", exception.getMessage());
    }
}