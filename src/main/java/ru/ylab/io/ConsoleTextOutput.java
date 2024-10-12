package ru.ylab.io;

public class ConsoleTextOutput implements TextOutput {
    @Override
    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    @Override
    public void print(String line) {
        System.out.print(line);
    }

    @Override
    public void println(String line) {
        System.out.println(line);
    }
}