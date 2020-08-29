package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.Groups.Delete;

import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestDeleteProfilePictureGroup
{
    @JsonProperty ("id_group")
    private ID idGroup;

    @JsonProperty ("id_profile_picture")
    private ID idProfilePicture;

    public RequestDeleteProfilePictureGroup ()
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

    public ID getIdProfilePicture ()
    {
        return idProfilePicture;
    }

    public void setIdProfilePicture (ID idProfilePicture)
    {
        this.idProfilePicture = idProfilePicture;
    }
}
