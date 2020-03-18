package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.Security.Management.Change.ChangeUsername;

import com.bardiademon.CyrusMessenger.bardiademon.ID;

public final class RequestChangeUsernameGroup
{
    private ID id;
    private String username;

    public RequestChangeUsernameGroup ()
    {
    }

    public ID getId ()
    {
        return id;
    }

    public void setId (ID id)
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
