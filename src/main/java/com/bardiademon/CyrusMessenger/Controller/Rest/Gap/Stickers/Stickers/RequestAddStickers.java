package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Stickers.Stickers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

public final class RequestAddStickers
{
    private String id;
    private String id_group;
    private String name;

    @JsonIgnore
    private MultipartFile image;

    public RequestAddStickers ()
    {
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getId_group ()
    {
        return id_group;
    }

    public void setId_group (String id_group)
    {
        this.id_group = id_group;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public MultipartFile getImage ()
    {
        return image;
    }

    public void setImage (MultipartFile image)
    {
        this.image = image;
    }
}
