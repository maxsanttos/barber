package com.max.barber.model.user.dtos;

import com.max.barber.model.user.RoleUser;

public record RegisterDTO(String username, String password, RoleUser role) {
    
}
