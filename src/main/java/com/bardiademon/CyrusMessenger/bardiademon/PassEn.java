package com.bardiademon.CyrusMessenger.bardiademon;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class PassEn
{
    public static String encoder (String Password)
    {
        return ((PasswordEncoder) new BCryptPasswordEncoder ()).encode (Password);
    }
}
