package ru.ylab.repository;

import ru.ylab.model.Admin;

import java.util.Map;

public class AdminRepository {
    private static final Map<String, Admin> admins = Map.of("admin", new Admin("admin", "password"));

    public Admin loginAsAdmin(String username, String password) {
        if (!admins.containsKey(username)) {
            throw new IllegalArgumentException("Админа с таким username не существует");
        }
        if (!admins.get(username).getPassword().equals(password)) {
            throw new IllegalArgumentException("Неверный пароль");
        }

        return admins.get(username);
    }
}