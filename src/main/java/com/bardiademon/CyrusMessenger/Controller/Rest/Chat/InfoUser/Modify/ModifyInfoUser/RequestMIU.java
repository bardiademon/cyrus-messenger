package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Modify.ModifyInfoUser;

import com.fasterxml.jackson.annotation.JsonIgnore;

// MIU => Modify Info User
public final class RequestMIU
{
    private String bio, name, family, mylink, gender;

    @JsonIgnore
    private boolean updatedBio;

    @JsonIgnore
    private boolean updatedName;

    @JsonIgnore
    private boolean updatedFamily;

    @JsonIgnore
    private boolean updatedUsername;

    @JsonIgnore
    private boolean updatedMylink;

    @JsonIgnore
    private boolean updatedGender;

    public RequestMIU ()
    {
    }

    public String getBio ()
    {
        return bio;
    }

    public void setBio (String bio)
    {
        this.bio = bio;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getFamily ()
    {
        return family;
    }

    public void setFamily (String family)
    {
        this.family = family;
    }

    public String getMylink ()
    {
        return mylink;
    }

    public void setMylink (String mylink)
    {
        this.mylink = mylink;
    }

    public boolean isUpdatedBio ()
    {
        return updatedBio;
    }

    public void setUpdatedBio ()
    {
        this.updatedBio = true;
    }

    public boolean isUpdatedName ()
    {
        return updatedName;
    }

    public void setUpdatedName ()
    {
        this.updatedName = true;
    }

    public boolean isUpdatedFamily ()
    {
        return updatedFamily;
    }

    public void setUpdatedFamily ()
    {
        this.updatedFamily = true;
    }

    public boolean isUpdatedUsername ()
    {
        return updatedUsername;
    }

    public void setUpdatedUsername ()
    {
        this.updatedUsername = true;
    }

    public boolean isUpdatedMylink ()
    {
        return updatedMylink;
    }

    public void setUpdatedMylink ()
    {
        this.updatedMylink = true;
    }

    public void setUpdatedGender ()
    {
        this.updatedGender = true;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public boolean isUpdatedGender ()
    {
        return updatedGender;
    }


}
