package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Contacts;


import com.fasterxml.jackson.annotation.JsonIgnore;

public final class RequestAddContacts
{

    @JsonIgnore
    public static final int MIN_PHONE = 10;

    @JsonIgnore
    public static final int MAX_PHONE = 20;

    private String name, family, phone, region;

    public RequestAddContacts ()
    {
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

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getRegion ()
    {
        return region;
    }

    public void setRegion (String region)
    {
        this.region = region;
    }
}
