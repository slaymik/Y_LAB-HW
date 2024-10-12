package ru.ylab;

import ru.ylab.controllers.MainController;
import ru.ylab.io.ConsoleTextInput;
import ru.ylab.io.ConsoleTextOutput;
import ru.ylab.utils.StreakResetScheduler;

public class Main {
    private final static ConsoleTextInput INPUT = new ConsoleTextInput();
    private final static ConsoleTextOutput OUTPUT = new ConsoleTextOutput();
    private final static MainController MAIN_CONTROLLER = new MainController();
    private final static StreakResetScheduler SCHEDULER = new StreakResetScheduler();

    public static void main(String[] args) {
        SCHEDULER.startScheduler();
        MAIN_CONTROLLER.start(OUTPUT, INPUT);
    }
}