package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.Typing;

import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.PublicRequest;

public final class ReqTyping extends PublicRequest
{
    /**
     * from , to => username
     */
    private String from;
    private String to;

    public ReqTyping ()
    {
    }

    public String getFrom ()
    {
        return from;
    }

    public void setFrom (String from)
    {
        this.from = from;
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
