package ru.ylab.controllers;

import ru.ylab.io.TextInput;
import ru.ylab.io.TextOutput;
import ru.ylab.model.User;
import ru.ylab.services.UserService;

public class UserController {
    private User currentUser;
    private final UserService userService = new UserService();
    private final HabitController habitController = new HabitController();

    public void registrationMenu(TextOutput output, TextInput input) {
        output.println("Регистрация:");
        output.print("Введите email: ");
        String email = input.nextLine();
        output.print("Введите пароль (минимум 8 символов): ");
        String password = input.nextLine();
        output.print("Введите имя: ");
        String name = input.nextLine();
        currentUser = userService.registerUser(email, password, name);
        output.println("Регистрация успешна!\n");
        loginMenu(output, input);
    }

    public void loginMenu(TextOutput output, TextInput input) {
        output.println("Вход:");
        output.print("Введите email: ");
        String email = input.nextLine();
        output.print("Введите пароль: ");
        String password = input.nextLine();
        User user = userService.loginUser(email, password);
        if (user != null) {
            output.println("Вход успешен!\n");
            currentUser = user;
            userMenu(output, input);
        } else {
            output.println("Неправильный email или пароль.");
            loginMenu(output, input);
        }
    }


    public void userMenu(TextOutput output, TextInput input) {
        output.println("Пользовательское меню:");
        output.println("1. Редактировать профиль");
        output.println("2. Удалить аккаунт");
        output.println("3. Сбросить пароль");
        output.println("4. Перейти к привычкам");
        int choice = input.nextInt();
        try {
            switch (choice) {
                case 1 -> editProfileMenu(output, input);
                case 2 -> deleteAccountMenu(output);
                case 3 -> resetPasswordMenu(output, input);
                case 4 -> habitController.habitMenu(currentUser, output, input);
                default -> throw new IllegalArgumentException("Неправильный выбор. Пожалуйста, попробуйте еще раз.");
            }
        } catch (Exception e) {
            output.println("\nПроизошла ошибка: %s\n".formatted(e.getMessage()));
        } finally {
            userMenu(output, input);
        }
    }

    private void editProfileMenu(TextOutput output, TextInput input) {
        output.println("Редактировать профиль:");
        output.print("Введите новое имя: ");
        String name = input.nextLine();
        output.print("Введите новый email: ");
        String email = input.nextLine();
        output.print("Введите новый пароль: ");
        String password = input.nextLine();
        userService.editUser(currentUser.getEmail(), email, name, password);
        output.println("Профиль обновлен!");
        userMenu(output, input);
    }

    private void deleteAccountMenu(TextOutput output) {
        output.println("Удалить аккаунт:");
        userService.deleteUser(currentUser.getEmail());
        output.println("Аккаунт удален!");
        System.exit(0);
    }

    private void resetPasswordMenu(TextOutput output, TextInput input) {
        output.println("Сбросить пароль:");
        output.print("Введите новый пароль: ");
        String newPassword = input.nextLine();
        userService.resetPassword(currentUser.getEmail(), currentUser.getPassword(), newPassword);
        output.println("Пароль сброшен!");
        userMenu(output, input);
    }
}