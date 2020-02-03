package com.bardiademon.CyrusMessenger.Controller.Rest.Cookie;

import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;

import javax.servlet.http.Cookie;

public final class MCookie
{

    private MCookie ()
    {
    }

    public static final String KEY_CODE_LOGIN_COOKIE = "code_login";

    public static Cookie CookieApi (String value)
    {
        Cookie cookie = new Cookie (KEY_CODE_LOGIN_COOKIE , value);
        cookie.setDomain (Domain.DOMAIN);
        cookie.setPath (Domain.RN_MAIN_API);
        return cookie;
    }
}
