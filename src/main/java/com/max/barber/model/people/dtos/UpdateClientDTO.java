package com.max.barber.model.people.dtos;

import com.max.barber.model.user.RoleUser;

public record  UpdateClientDTO(String username, String phoneNumber, RoleUser role) {
   
}