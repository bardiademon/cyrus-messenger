package com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login;

import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP.IsValidUEPRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

public final class LoginRequest extends IsValidUEPRequest
{
    private String password;

    public LoginRequest (String uep , String region , String password)
    {
        super (uep , region);
        this.password = password;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    @JsonIgnore
    public IsValidUEPRequest getIsValidUEPRequest ()
    {
        return this;
    }
}
