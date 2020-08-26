package com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.OnlineStatus;

import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.PublicRequest;

public final class RequestOnlineStatus extends PublicRequest
{
    private String username;

    public RequestOnlineStatus ()
    {
        super ();
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
