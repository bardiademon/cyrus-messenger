package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.GetAll;

public class RequestGetAllProfilePicture
{
    private long id;

    private String username;

    public RequestGetAllProfilePicture ()
    {
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }
}
