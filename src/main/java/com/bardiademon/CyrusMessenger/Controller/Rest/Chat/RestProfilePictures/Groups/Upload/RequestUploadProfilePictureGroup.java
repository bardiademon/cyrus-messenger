package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Groups.Upload;

import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Users.Upload.RequestUploadProfilePictureUser;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.fasterxml.jackson.annotation.JsonIgnore;

public final class RequestUploadProfilePictureGroup extends RequestUploadProfilePictureUser
{

    private String id_group;

    @JsonIgnore
    private ID idGroup;

    public RequestUploadProfilePictureGroup ()
    {
        super ();
    }

    public String getId_group ()
    {
        return id_group;
    }

    public void setId_group (String id_group)
    {
        this.id_group = id_group;
        setIdGroup (new ID (this.id_group));
    }

    @JsonIgnore
    public ID getIdGroup ()
    {
        return idGroup;
    }

    public void setIdGroup (ID idGroup)
    {
        this.idGroup = idGroup;
    }
}
