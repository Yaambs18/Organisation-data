package com.relationaldata.organisationdata.login;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {

        String hashed = BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(14));

        return hashed;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}
