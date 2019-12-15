package com.bardiademon.CyrusMessenger.Controller.RestLogin.IsValidUEP;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IsValidUEPRequest
{

    @JsonIgnore
    public static final String USERNAME = "username", EMAIL = "email", PHONE = "phone";

    private String valueUEP;

    // UEP => Username , Email , Phone
    public final String uep;

    public final String region;

    public IsValidUEPRequest ()
    {
        this (null , null);
    }

    public IsValidUEPRequest (String uep , String region)
    {
        this.uep = uep;
        this.region = region;
    }

    public String getValueUEP ()
    {
        return valueUEP;
    }

    public void setValueUEP (String valueUEP)
    {
        this.valueUEP = valueUEP;
    }
}
