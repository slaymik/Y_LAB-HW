package ru.ylab.repository;

import ru.ylab.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private static final Map<String, User> users = new HashMap<>();

    public User registerUser(String email, String password, String name) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Неверный формать email");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Неверный формат пароля");
        }
        if (users.containsKey(email)) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        User user = new User(email, password, name, false);
        users.put(email, user);
        return user;
    }

    public User loginUser(String email, String password) {
        if (!users.containsKey(email)) {
            throw new IllegalArgumentException("Неверный email");
        }
        if (!users.get(email).getPassword().equals(password)) {
            throw new IllegalArgumentException("Неверный пароль");
        }

        if (getUser(email).isBlocked()) {
            throw new IllegalArgumentException("Пользователь заблокирован");
        }

        return getUser(email);
    }

    public void editUser(String oldEmail, String newEmail, String newName, String newPassword) {
        User user = getUser(oldEmail);

        if (user != null) {
            user.setName(newName);

            if (!isValidEmail(newEmail)) {
                throw new IllegalArgumentException("Неверный формать email");
            }
            user.setEmail(newEmail);
            users.remove(oldEmail);
            users.put(newEmail, user);

            if (!isValidPassword(newPassword)) {
                throw new IllegalArgumentException("Неверный формат пароля");
            }
            user.setPassword(newPassword);
        }
    }

    public User getUser(String email) {
        if (!users.containsKey(email)) {
            throw new IllegalArgumentException("Пользователя с таким email не существует");
        }
        return users.get(email);
    }

    public void deleteUser(String email) {
        users.remove(email);
    }

    public void resetPassword(String email, String oldPassword, String newPassword) {
        User user = getUser(email);

        if (user != null && user.getPassword().equals(oldPassword)) {
            if (!isValidPassword(newPassword)) {
                throw new IllegalArgumentException("Неверный формат пароля");
            }
            user.setPassword(newPassword);
        }
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void blockUser(String email) {
        if (!users.containsKey(email)) {
            throw new IllegalArgumentException("Пользователя с таким email не существует");
        }

        users.get(email).setBlocked(true);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = ".{8,}$";
        return password.matches(passwordRegex);
    }

    public void clear(){
        users.clear();
    }
}