package com.max.barber.model;

public enum RoleUser {
    CLIENTE("CLIENTE"),
    ADMIN("ADMIN");
    


    private final String role;

    RoleUser(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
