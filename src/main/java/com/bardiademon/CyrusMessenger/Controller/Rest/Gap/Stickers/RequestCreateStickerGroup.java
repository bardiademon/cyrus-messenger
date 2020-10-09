package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Stickers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public final class RequestCreateStickerGroup
{

    private long id;

    private String group_name;

    @JsonIgnore
    private MultipartFile group_image;

    private String description;

    @JsonProperty ("with_per")
    private String with_per;

    @JsonIgnore
    private boolean withPermission;

    /**
     * MainAccount.username
     */
    private List <String> licensed_users;

    public RequestCreateStickerGroup ()
    {
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
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

    public String getWith_per ()
    {
        return with_per;
    }

    public void setWith_per (String with_per)
    {
        this.with_per = with_per;
    }

    public boolean isWithPermission ()
    {
        return withPermission;
    }

    public void setWithPermission (boolean withPermission)
    {
        this.withPermission = withPermission;
    }

    public List <String> getLicensed_users ()
    {
        return licensed_users;
    }

    public void setLicensed_users (List <String> licensed_users)
    {
        this.licensed_users = licensed_users;
    }
}
