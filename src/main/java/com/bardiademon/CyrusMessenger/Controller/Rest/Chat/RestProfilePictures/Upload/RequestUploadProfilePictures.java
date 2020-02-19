package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Upload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

public final class RequestUploadProfilePictures
{

    private long id;

    private boolean updateMainPic;

    private boolean mainPic;

    private int placementNumber = -1;

    private MultipartFile pic;

    private long idGroup;

    public RequestUploadProfilePictures ()
    {
    }

    public boolean isMainPic ()
    {
        return mainPic;
    }

    public void setMainPic (boolean mainPic)
    {
        this.mainPic = mainPic;
    }

    public int getPlacementNumber ()
    {
        return placementNumber;
    }

    public void setPlacementNumber (int placementNumber)
    {
        this.placementNumber = placementNumber;
    }

    @JsonIgnore
    public MultipartFile getPic ()
    {
        return pic;
    }

    public void setPic (MultipartFile pic)
    {
        this.pic = pic;
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public boolean isUpdateMainPic ()
    {
        return updateMainPic;
    }

    public void setUpdateMainPic (boolean updateMainPic)
    {
        this.updateMainPic = updateMainPic;
    }

    public long getIdGroup ()
    {
        return idGroup;
    }

    public void setIdGroup (long idGroup)
    {
        this.idGroup = idGroup;
    }
}
