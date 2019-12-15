package com.bardiademon.CyrusMessenger.Controller.RestLogin.Login;

import com.bardiademon.CyrusMessenger.Controller.RestLogin.IsValidUEP.IsValidUEPRequest;

public final class LoginRequest extends IsValidUEPRequest
{
    private String password;

    public LoginRequest (String uep , String region , String password)
    {
        super (uep , region);
        this.password = password;
    }

    public LoginRequest (String uep , String region)
    {
        super (uep , region);
    }

    public LoginRequest ()
    {
        super ();
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public IsValidUEPRequest getIsValidUEPRequest ()
    {
        return this;
    }
}
