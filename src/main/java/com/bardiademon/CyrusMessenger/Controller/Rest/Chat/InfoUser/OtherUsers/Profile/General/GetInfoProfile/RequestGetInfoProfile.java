package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.OtherUsers.Profile.General.GetInfoProfile;

import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Get.General.RequestGeneral;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestGetInfoProfile extends RequestGeneral
{
    @JsonProperty ("id_user")
    private long idUser;

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


}
