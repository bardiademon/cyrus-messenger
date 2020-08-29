package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.Users.Upload;

import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class RequestUploadProfilePictureUser
{
    private MultipartFile picture;
    private boolean main;
    private int placement_number;
    private boolean replace;
    private String id;

    private boolean update_main;

    private boolean separate;
    private List <String> who;

    @JsonIgnore
    private ID idProfilePicture;

    public RequestUploadProfilePictureUser ()
    {
    }

    @JsonIgnore
    public MultipartFile getPicture ()
    {
        return picture;
    }

    public void setPicture (MultipartFile picture)
    {
        this.picture = picture;
    }

    public boolean isMain ()
    {
        return main;
    }

    public void setMain (boolean main)
    {
        this.main = main;
    }

    public int getPlacement_number ()
    {
        return placement_number;
    }

    public void setPlacement_number (int placement_number)
    {
        this.placement_number = placement_number;
    }

    public boolean isReplace ()
    {
        return replace;
    }

    public void setReplace (boolean replace)
    {
        this.replace = replace;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
        idProfilePicture = new ID (id);
    }

    @JsonIgnore
    public ID getIdProfilePicture ()
    {
        return idProfilePicture;
    }

    public boolean isUpdate_main ()
    {
        return update_main;
    }

    public void setUpdate_main (boolean update_main)
    {
        this.update_main = update_main;
    }

    public boolean isSeparate ()
    {
        return separate;
    }

    public void setSeparate (boolean separate)
    {
        this.separate = separate;
    }

    public List <String> getWho ()
    {
        return who;
    }

    public void setWho (List <String> who)
    {
        this.who = who;
    }
}
