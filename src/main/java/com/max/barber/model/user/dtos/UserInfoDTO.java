package com.max.barber.model.user.dtos;

import com.max.barber.model.user.RoleUser;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserInfoDTO {
    private Long user_id;
    private String username;
    private RoleUser role;
}
