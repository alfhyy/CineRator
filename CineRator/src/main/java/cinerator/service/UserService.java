package cinerator.service;

import cinerator.model.User;
import cinerator.storage.ExcelStorage;

import java.util.UUID;

public class UserService {

    private final ExcelStorage storage;

    public UserService(ExcelStorage storage) {
        this.storage = storage;
    }

    public User login(String username) {
        User user = storage.findUserByUsername(username);
        if (user != null) return user;

        User newUser = new User(UUID.randomUUID().toString(), username);
        storage.saveUser(newUser);
        return newUser;
    }
}
