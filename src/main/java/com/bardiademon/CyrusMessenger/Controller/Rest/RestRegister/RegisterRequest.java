package com.bardiademon.CyrusMessenger.Controller.Rest.RestRegister;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class RegisterRequest
{

    public final String EMAIL_NULL = "nl";

    private String username;
    private String name, family;

    private String password;

    @JsonProperty ("code_confirmed_phone")
    private String codeConfirmedPhone;

    /**
     * nl => agar in bashad barasi nemishavad
     * agar hata khali bashad yani darone darkhast ersal shode
     */
    @JsonProperty ("code_confirmed_email")
    private String codeEmail = EMAIL_NULL;

    public RegisterRequest ()
    {
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

    public String getCodeEmail ()
    {
        return codeEmail;
    }

    public void setCodeEmail (String codeEmail)
    {
        this.codeEmail = codeEmail;
    }
}

