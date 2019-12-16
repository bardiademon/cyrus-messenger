package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.GetInfoUser;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestInfoUser
{
    @JsonProperty ("get_name")
    private boolean getName;

    @JsonProperty ("get_family")
    private boolean getFamily;

    @JsonProperty ("get_username")
    private boolean getUsername;

    @JsonProperty ("get_email")
    private boolean getEmail;

    @JsonProperty ("get_phone")
    private boolean getPhone;

    public RequestInfoUser ()
    {
    }

    public RequestInfoUser (boolean getName , boolean getFamily , boolean getUsername , boolean getEmail , boolean getPhone)
    {
        this.getName = getName;
        this.getFamily = getFamily;
        this.getUsername = getUsername;
        this.getEmail = getEmail;
        this.getPhone = getPhone;
    }

    public boolean atLeastOne ()
    {
        return (isGetName () || isGetFamily () || isGetUsername () || isGetEmail () || isGetPhone ());
    }

    public boolean isGetName ()
    {
        return getName;
    }

    public void setGetName (boolean getName)
    {
        this.getName = getName;
    }

    public boolean isGetFamily ()
    {
        return getFamily;
    }

    public void setGetFamily (boolean getFamily)
    {
        this.getFamily = getFamily;
    }

    public boolean isGetUsername ()
    {
        return getUsername;
    }

    public void setGetUsername (boolean getUsername)
    {
        this.getUsername = getUsername;
    }

    public boolean isGetEmail ()
    {
        return getEmail;
    }

    public void setGetEmail (boolean getEmail)
    {
        this.getEmail = getEmail;
    }

    public boolean isGetPhone ()
    {
        return getPhone;
    }

    public void setGetPhone (boolean getPhone)
    {
        this.getPhone = getPhone;
    }
}
