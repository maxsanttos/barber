package com.max.barber.model.user;

public enum RoleUser {
    CLIENT("CLIENT"),
    ADMIN("ADMIN"),
    BARBER("BARBER");
    
    private final String role;

    RoleUser(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
