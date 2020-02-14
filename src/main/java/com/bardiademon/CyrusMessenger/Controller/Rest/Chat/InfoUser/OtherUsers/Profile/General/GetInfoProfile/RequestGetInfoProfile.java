package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.OtherUsers.Profile.General.GetInfoProfile;

import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Get.General.RequestGeneral;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestGetInfoProfile extends RequestGeneral
{
    @JsonProperty ("id_user")
    private long idUser;

    private String username;

    public RequestGetInfoProfile ()
    {
        super ();
    }

    public long getIdUser ()
    {
        return idUser;
    }

    public void setIdUser (long idUser)
    {
        this.idUser = idUser;
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
