package ru.ylab.controllers;

import ru.ylab.io.TextInput;
import ru.ylab.io.TextOutput;
import ru.ylab.model.Admin;
import ru.ylab.services.AdminService;

public class AdminController {
    private final AdminService adminService = new AdminService();

    public void adminMenu(TextOutput output, TextInput input) {
        output.println("Административное меню:");
        output.println("1. Просмотреть всех пользователей");
        output.println("2. Просмотреть привычки пользователя");
        output.println("3. Удалить пользователя");
        output.println("4. Заблокировать пользователя");
        int choice = input.nextInt();

        switch (choice) {
            case 1 -> {
                viewAllUsersMenu(output);
                adminMenu(output, input);
            }
            case 2 -> viewUserHabitsMenu(output, input);
            case 3 -> deleteUserMenu(output, input);
            case 4 -> blockUserMenu(output, input);
            default -> {
                output.println("Неправильный выбор. Пожалуйста, попробуйте еще раз.");
                adminMenu(output, input);
            }
        }
    }

    public void loginAsAdminMenu(TextOutput output, TextInput input) {
        output.println("Вход:");
        output.print("Введите username: ");
        String username = input.nextLine();
        output.print("Введите пароль: ");
        String password = input.nextLine();
        Admin admin = adminService.loginAsAdmin(username, password);
        if (admin != null) {
            output.println("Вход успешен!\n");
            adminMenu(output, input);
        } else {
            output.println("Неправильный username или пароль.");
            loginAsAdminMenu(output, input);
        }
    }

    private void viewAllUsersMenu(TextOutput output) {
        output.println("\nВсе пользователи:");
        output.println(adminService.getAllUsers());
    }

    private void viewUserHabitsMenu(TextOutput output, TextInput input) {
        viewAllUsersMenu(output);
        output.print("\nВведите email пользователя: ");
        String email = input.nextLine();
        output.println("Привычки пользователя:");
        output.println(adminService.getAllHabits(email));
        adminMenu(output, input);
    }

    private void deleteUserMenu(TextOutput output, TextInput input) {
        viewAllUsersMenu(output);
        output.print("Введите email пользователя: ");
        String email = input.nextLine();
        adminService.deleteUser(email);
        output.println("Пользователь удален!\n");
        adminMenu(output, input);
    }

    private void blockUserMenu(TextOutput output, TextInput input) {
        viewAllUsersMenu(output);
        output.print("\nВведите email пользователя: ");
        String email = input.nextLine();
        adminService.blockUser(email);
        output.println("\nПользователь заблокирован!\n");
        adminMenu(output, input);
    }
}