package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Groups.CreateGroup;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestCreateGroup
{
    private String name, description, bio, link, groupname;

    @JsonProperty ("create_link_join")
    private boolean createLinkJoin;

    @JsonProperty ("family_group")
    private boolean familyGroup;

    @JsonProperty ("is_channel")
    private boolean isChannel = false;

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

    public String getGroupname ()
    {
        return groupname;
    }

    public void setGroupname (String groupname)
    {
        this.groupname = groupname;
    }

    public boolean isCreateLinkJoin ()
    {
        return createLinkJoin;
    }

    public void setCreateLinkJoin (boolean createLinkJoin)
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

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public boolean isChannel ()
    {
        return isChannel;
    }

    public void setChannel (boolean channel)
    {
        isChannel = channel;
    }
}
