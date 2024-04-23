package com.khipo.desafiojava.controller.dto.auth;

import com.khipo.desafiojava.enums.UserRoleEnum;

public record RegisterDto(String login, String password, UserRoleEnum role) {
}
