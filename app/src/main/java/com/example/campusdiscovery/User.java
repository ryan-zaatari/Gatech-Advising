package com.example.campusdiscovery;

public class User {
    private final String name;
    private final String password;
    private final UserType userType;

    public User(String name, String password, UserType userType) {
        this.name = name;
        this.password = password;
        this.userType = userType;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public String getName() {
        return name;
    }

    public UserType getUserType() {
        return userType;
    }
}
