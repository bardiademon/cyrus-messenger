package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.GetInfoUser.General;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestGeneral
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

    @JsonProperty ("get_mylink")
    private boolean getMyLink;

    @JsonProperty ("get_bio")
    private boolean getBio;

    public RequestGeneral ()
    {
    }

    public RequestGeneral (boolean getName , boolean getFamily , boolean getUsername , boolean getEmail , boolean getPhone , boolean getMyLink , boolean getBio)
    {
        this.getName = getName;
        this.getFamily = getFamily;
        this.getUsername = getUsername;
        this.getEmail = getEmail;
        this.getPhone = getPhone;
        this.getMyLink = getMyLink;
        this.getBio = getBio;
    }

    public boolean atLeastOne ()
    {
        return (isGetName () || isGetFamily () || isGetUsername () || isGetEmail () || isGetPhone () || isGetMyLink () || isGetBio ());
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

    public boolean isGetMyLink ()
    {
        return getMyLink;
    }

    public void setGetMyLink (boolean getMyLink)
    {
        this.getMyLink = getMyLink;
    }

    public boolean isGetBio ()
    {
        return getBio;
    }

    public void setGetBio (boolean getBio)
    {
        this.getBio = getBio;
    }
}
