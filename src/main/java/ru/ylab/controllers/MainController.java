package ru.ylab.controllers;

import ru.ylab.io.TextInput;
import ru.ylab.io.TextOutput;

public class MainController {
    private final UserController userController = new UserController();
    private final AdminController adminController = new AdminController();

    public void start(TextOutput output, TextInput input) {
        try {
            while (true) {
                output.println("Вход и регистрация:");
                output.println("1. Регистрация");
                output.println("2. Вход");
                output.println("3. Вход в качестве админа");
                output.println("4. Выход");
                int choice = input.nextInt();
                switch (choice) {
                    case 1 -> userController.registrationMenu(output, input);
                    case 2 -> userController.loginMenu(output, input);
                    case 3 -> adminController.loginAsAdminMenu(output, input);
                    case 4 -> System.exit(0);
                    default -> {
                        output.println("Неправильный выбор. Пожалуйста, попробуйте еще раз.\n");
                        start(output, input);
                    }
                }
            }
        } catch (Exception e) {
            output.println("\nПроизошла ошибка: %s\n".formatted(e.getMessage()));
        } finally {
            start(output, input);
        }
    }
}