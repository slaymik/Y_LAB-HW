package ru.ylab.services;

import lombok.AllArgsConstructor;
import ru.ylab.model.Admin;
import ru.ylab.repository.AdminRepository;
import ru.ylab.repository.UserRepository;
@AllArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository =  new AdminRepository();
    private final UserRepository userRepository = new UserRepository();
    private final HabitService habitService = new HabitService();

    public Admin loginAsAdmin(String username, String password){
        return adminRepository.loginAsAdmin(username, password);
    }

    public String getAllUsers(){
        StringBuilder builder = new StringBuilder();
        userRepository.getAllUsers().forEach(builder::append);
        return builder.toString();
    }

    public String getAllHabits(String email){
        StringBuilder builder = new StringBuilder();
        habitService.getAllHabits(userRepository.getUser(email)).forEach(builder::append);
        return builder.toString();
    }

    public void deleteUser(String email){
        userRepository.deleteUser(email);
    }

    public void blockUser(String email){
        userRepository.blockUser(email);
    }
}