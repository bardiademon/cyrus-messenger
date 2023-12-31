package com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IsValidUEPRequest
{

    @JsonIgnore
    public static final String USERNAME = "username", EMAIL = "email", PHONE = "phone";

    private String valueUEP;

    // UEP => Username , Email , Phone
    private String uep;

    public final String region;

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

    public String getUep ()
    {
        return uep;
    }

    public void setUep (String uep)
    {
        this.uep = uep;
    }
}
