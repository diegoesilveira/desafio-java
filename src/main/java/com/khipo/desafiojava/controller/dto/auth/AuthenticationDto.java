package com.khipo.desafiojava.controller.dto.auth;

import com.khipo.desafiojava.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AuthenticationDto {

    private String login;
    private String password;
    private UserRoleEnum role;
}
