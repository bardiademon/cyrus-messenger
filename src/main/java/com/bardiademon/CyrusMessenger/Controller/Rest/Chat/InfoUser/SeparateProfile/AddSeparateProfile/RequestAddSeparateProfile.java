package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.SeparateProfile.AddSeparateProfile;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public final class RequestAddSeparateProfile
{
    private String bio, family, mylink, name;

    @JsonProperty ("profile_for")
    private List <String> profileFor;

    public RequestAddSeparateProfile ()
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

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public List <String> getProfileFor ()
    {
        return profileFor;
    }

    public void setProfileFor (List <String> profileFor)
    {
        this.profileFor = profileFor;
    }
}
