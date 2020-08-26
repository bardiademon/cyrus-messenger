package com.bardiademon.CyrusMessenger.ServerSocket.RestSocket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PublicRequest
{
    @JsonProperty ("code_login")
    private String codeLogin;

    @JsonProperty ("code_online")
    private String codeOnline;

    public PublicRequest ()
    {
    }

    public String getCodeLogin ()
    {
        return codeLogin;
    }

    public void setCodeLogin (String codeLogin)
    {
        this.codeLogin = codeLogin;
    }

    public String getCodeOnline ()
    {
        return codeOnline;
    }

    public void setCodeOnline (String codeOnline)
    {
        this.codeOnline = codeOnline;
    }
}
