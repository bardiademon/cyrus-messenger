package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.Security.Management.SuspendManager;

import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestSuspendManager
{
    @JsonProperty ("id_group")
    private ID idGroup;

    @JsonProperty ("id_user")
    private ID idUser;

    public RequestSuspendManager ()
    {
    }

    public ID getIdGroup ()
    {
        return idGroup;
    }

    public void setIdGroup (ID idGroup)
    {
        this.idGroup = idGroup;
    }

    public ID getIdUser ()
    {
        return idUser;
    }

    public void setIdUser (ID idUser)
    {
        this.idUser = idUser;
    }
}
