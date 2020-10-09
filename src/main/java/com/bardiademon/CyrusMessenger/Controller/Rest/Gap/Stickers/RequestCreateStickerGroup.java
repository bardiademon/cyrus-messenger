package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Stickers;

import org.springframework.web.multipart.MultipartFile;

public final class RequestCreateStickerGroup
{

    private String group_name;

    private MultipartFile group_image;

    private String description;

    public RequestCreateStickerGroup ()
    {
    }

    public String getGroup_name ()
    {
        return group_name;
    }

    public void setGroup_name (String group_name)
    {
        this.group_name = group_name;
    }

    public MultipartFile getGroup_image ()
    {
        return group_image;
    }

    public void setGroup_image (MultipartFile group_image)
    {
        this.group_image = group_image;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }
}
