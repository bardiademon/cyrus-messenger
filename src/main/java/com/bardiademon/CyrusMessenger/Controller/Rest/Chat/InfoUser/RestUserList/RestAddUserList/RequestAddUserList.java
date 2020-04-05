package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.RestUserList.RestAddUserList;

import com.bardiademon.CyrusMessenger.Controller.Rest.UsedRequests.R_IDUsername;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestAddUserList
{
    @JsonProperty("user")
    private R_IDUsername idUsername;

    private String type;

    private boolean change;

    public RequestAddUserList ()
    {
    }

    public R_IDUsername getIdUsername ()
    {
        return idUsername;
    }

    public void setIdUsername (R_IDUsername idUsername)
    {
        this.idUsername = idUsername;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public boolean isChange ()
    {
        return change;
    }

    public void setChange (boolean change)
    {
        this.change = change;
    }
}
