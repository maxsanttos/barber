package com.max.barber.model.people.dtos;

import com.max.barber.model.user.RoleUser;

public record UpdateBarberDTO(String username, String password, String specialty, RoleUser role) {
}
