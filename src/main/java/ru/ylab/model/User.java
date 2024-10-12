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
    private boolean isBlocked;

    @Override
    public String toString() {
        return "User  {email='%s', name='%s', isBlocked=%s}".formatted(email, name, isBlocked);
    }
}