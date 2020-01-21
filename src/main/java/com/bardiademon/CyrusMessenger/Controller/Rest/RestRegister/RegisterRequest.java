package com.bardiademon.CyrusMessenger.Controller.Rest.RestRegister;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class RegisterRequest
{

    private String username;
    private String name, family;

    private String password;

    @JsonProperty ("code_confirmed_phone")
    private String codeConfirmedPhone;

    public RegisterRequest ()
    {
    }

    public RegisterRequest (String username , String name , String family , String password , String codeConfirmedPhone)
    {
        this.username = username;
        this.name = name;
        this.family = family;
        this.password = password;
        this.codeConfirmedPhone = codeConfirmedPhone;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getFamily ()
    {
        return family;
    }

    public void setFamily (String family)
    {
        this.family = family;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getCodeConfirmedPhone ()
    {
        return codeConfirmedPhone;
    }

    public void setCodeConfirmedPhone (String codeConfirmedPhone)
    {
        this.codeConfirmedPhone = codeConfirmedPhone;
    }
}

