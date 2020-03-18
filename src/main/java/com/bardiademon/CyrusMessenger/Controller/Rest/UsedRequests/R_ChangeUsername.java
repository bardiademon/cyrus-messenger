package com.bardiademon.CyrusMessenger.Controller.Rest.UsedRequests;

import com.bardiademon.CyrusMessenger.bardiademon.ID;

public final class R_ChangeUsername
{
    private ID id;
    private String username;

    public R_ChangeUsername ()
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
