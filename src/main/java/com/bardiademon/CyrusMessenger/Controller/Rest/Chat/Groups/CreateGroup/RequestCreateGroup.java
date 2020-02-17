package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.CreateGroup;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestCreateGroup
{
    private String name, bio, link, username;

    @JsonProperty ("create_link_join")
    private String createLinkJoin;

    @JsonProperty ("family_group")
    private boolean familyGroup;

    public RequestCreateGroup ()
    {
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getBio ()
    {
        return bio;
    }

    public void setBio (String bio)
    {
        this.bio = bio;
    }

    public String getLink ()
    {
        return link;
    }

    public void setLink (String link)
    {
        this.link = link;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getCreateLinkJoin ()
    {
        return createLinkJoin;
    }

    public void setCreateLinkJoin (String createLinkJoin)
    {
        this.createLinkJoin = createLinkJoin;
    }


    public boolean isFamilyGroup ()
    {
        return familyGroup;
    }

    public void setFamilyGroup (boolean familyGroup)
    {
        this.familyGroup = familyGroup;
    }
}
