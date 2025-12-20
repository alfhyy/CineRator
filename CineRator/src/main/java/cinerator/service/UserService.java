package cinerator.service;

import cinerator.model.User;
import cinerator.storage.ExcelStorage;

import java.util.UUID;

public class UserService {

    private final ExcelStorage storage;

    public UserService(ExcelStorage storage) {
        this.storage = storage;
    }

    public User login(String username, String password) {

        User user = storage.findUserByUsername(username);

        // existing user → validate password
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user;
            }
            return null; // wrong password
        }

        // new user → auto register
        User newUser = new User(
                UUID.randomUUID().toString(),
                username,
                password
        );

        storage.saveUser(newUser);
        return newUser;
    }
}
