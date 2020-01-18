package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.New.General;

public final class RequestGeneral
{
    private String bio;
    private String name;
    private String family;
    private String username;
    private String mylink;


    private boolean updatedBio;
    private boolean updatedName;
    private boolean updatedFamily;
    private boolean updatedUsername;
    private boolean updatedMylink;

    public RequestGeneral ()
    {
    }

    public RequestGeneral (String bio , String name , String family , String username , String mylink)
    {
        this.bio = bio;
        this.name = name;
        this.family = family;
        this.username = username;
        this.mylink = mylink;
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

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getMylink ()
    {
        return mylink;
    }

    public void setMylink (String mylink)
    {
        this.mylink = mylink;
    }

    public boolean thereIsAtLeastOneTrue ()
    {
        return (isNull (getBio ()) || isNull (getName ()) || isNull (getFamily ()) || isNull (getUsername ()) || isNull (getMylink ()));
    }

    public boolean isNull (String str)
    {
        return (str == null || str.isEmpty ());
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
}
