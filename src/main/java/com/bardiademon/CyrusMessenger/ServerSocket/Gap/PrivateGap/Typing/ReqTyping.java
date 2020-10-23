package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.Typing;

import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.PublicRequest;

public final class ReqTyping extends PublicRequest
{
    /**
     * to => username
     */
    private String to;

    public ReqTyping ()
    {
    }

    public String getTo ()
    {
        return to;
    }

    public void setTo (String to)
    {
        this.to = to;
    }
}
