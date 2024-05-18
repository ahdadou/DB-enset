package com.DigitalBanking.security.models;

import lombok.Data;

@Data
public class LoginUserDto {
    private String email;

    private String password;
}