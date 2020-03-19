package com.bardiademon.CyrusMessenger.Controller.Rest.UsedRequests;

import com.fasterxml.jackson.annotation.JsonInclude;

public class R_IDUsername
{

    private long id;

    @JsonInclude (value = JsonInclude.Include.NON_NULL)
    private String username;

    public R_IDUsername ()
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
