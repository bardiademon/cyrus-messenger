package com.bardiademon.CyrusMessenger.Controller.Rest.RestRegister;

public final class RegisterRequest
{

    private String phone;

    public final String username, email, region;
    public final String name, family;

    public final String password;

    public RegisterRequest (String phone , String username , String email , String region , String name , String family , String password)
    {
        this.phone = phone;
        this.username = username;
        this.email = email;
        this.region = region;
        this.name = name;
        this.family = family;
        this.password = password;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }
}

