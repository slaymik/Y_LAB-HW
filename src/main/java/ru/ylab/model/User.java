package ru.ylab.model;

import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;
}