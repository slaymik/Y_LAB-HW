package ru.ylab.services;

import ru.ylab.model.User;
import ru.ylab.repository.UserRepository;

public class UserService {
    private final UserRepository repository = new UserRepository();

    public void registerUser(String email, String password, String name) {
        repository.registerUser(email, password, name);
    }

    public User loginUser(String email, String password) {
        return repository.loginUser(email, password);
    }

    public void editUser(String oldEmail, String newEmail, String newName, String newPassword) {
        repository.editUser(oldEmail, newEmail, newName, newPassword);
    }

    public void deleteUser(String email) {
        repository.deleteUser(email);
    }

    public void resetPassword(String email, String oldPassword, String newPassword) {
        repository.resetPassword(email, oldPassword, newPassword);
    }

    public User getUser(String email){
        return repository.getUser(email);
    }
}