package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Upload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

public final class RequestProfilePictures
{

    private long id;

    private boolean updateMainPic;

    private String thisPicFor;

    private boolean mainPic;

    private int placementNumber = 0;

    private MultipartFile pic;

    // channel or group
    private String username;

    public RequestProfilePictures ()
    {
    }

    public String getThisPicFor ()
    {
        return thisPicFor;
    }

    public void setThisPicFor (String thisPicFor)
    {
        this.thisPicFor = thisPicFor;
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

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
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
}
