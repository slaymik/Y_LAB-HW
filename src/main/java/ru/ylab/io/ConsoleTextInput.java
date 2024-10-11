package ru.ylab.io;

import java.util.Scanner;

public class ConsoleTextInput implements TextInput{
    private final Scanner scanner;

    public ConsoleTextInput() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    @Override
    public int nextInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите числовое значение: ");
            scanner.nextLine();
        }
        int intValue = scanner.nextInt();
        scanner.nextLine();
        return intValue;
    }
}
