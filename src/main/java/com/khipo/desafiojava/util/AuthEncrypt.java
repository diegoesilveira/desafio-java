package com.khipo.desafiojava.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthEncrypt {

    public static String encodePassword(String password) {
        try {
            return new BCryptPasswordEncoder().encode(password);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
