package ru.ylab.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
public class HabitAction {
    private int habitId;
    private String habitName;
    private LocalDate actionDate;
    private boolean completed;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        String formattedDate = formatter.format(actionDate);
        String status = completed ? "Выполнена" : "Не выполнена";
        return String.format("Дата: %s, Название: %s, Статус: %s", formattedDate, getHabitName(), status);
    }
}