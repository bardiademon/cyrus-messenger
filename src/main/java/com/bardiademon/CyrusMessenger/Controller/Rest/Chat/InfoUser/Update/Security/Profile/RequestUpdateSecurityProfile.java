package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Update.Security.Profile;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestUpdateSecurityProfile
{
    @JsonProperty (value = "sec_bio")
    private String secBio;

    @JsonProperty ("sec_list_friends")
    private String secListFriends;

    @JsonProperty ("sec_cover")
    private String secCover;

    @JsonProperty ("sec_show_in_channel")
    private String secShowInChannel;

    @JsonProperty ("sec_show_in_group")
    private String secShowInGroup;

    @JsonProperty ("sec_show_profile")
    private String secShowProfile;

    @JsonProperty ("sec_show_in_search")
    private String secShowInSearch;

    @JsonProperty ("sec_show_last_seen")
    private String secShowLastSeen;

    @JsonProperty ("sec_show_mylink")
    private String secShowMyLink;

    @JsonProperty ("sec_show_username")
    private String secShowUsername;

    @JsonProperty ("sec_show_email")
    private String secShowEmail;

    @JsonProperty ("sec_show_family")
    private String secShowFamily;

    @JsonProperty ("sec_find_me")
    private String secFindMe;

    @JsonProperty ("sec_find_me_by_phone")
    private String secFindMeByPhone;

    @JsonProperty ("sec_show_gender")
    private String secShowGender;

    public RequestUpdateSecurityProfile ()
    {
    }

    public String getSecBio ()
    {
        return secBio;
    }

    public void setSecBio (String secBio)
    {
        this.secBio = secBio;
    }

    public String getSecListFriends ()
    {
        return secListFriends;
    }

    public void setSecListFriends (String secListFriends)
    {
        this.secListFriends = secListFriends;
    }

    public String getSecCover ()
    {
        return secCover;
    }

    public void setSecCover (String secCover)
    {
        this.secCover = secCover;
    }

    public String getSecShowInChannel ()
    {
        return secShowInChannel;
    }

    public void setSecShowInChannel (String secShowInChannel)
    {
        this.secShowInChannel = secShowInChannel;
    }

    public String getSecShowInGroup ()
    {
        return secShowInGroup;
    }

    public void setSecShowInGroup (String secShowInGroup)
    {
        this.secShowInGroup = secShowInGroup;
    }

    public String getSecShowProfile ()
    {
        return secShowProfile;
    }

    public void setSecShowProfile (String secShowProfile)
    {
        this.secShowProfile = secShowProfile;
    }

    public String getSecShowInSearch ()
    {
        return secShowInSearch;
    }

    public void setSecShowInSearch (String secShowInSearch)
    {
        this.secShowInSearch = secShowInSearch;
    }

    public String getSecShowLastSeen ()
    {
        return secShowLastSeen;
    }

    public void setSecShowLastSeen (String secShowLastSeen)
    {
        this.secShowLastSeen = secShowLastSeen;
    }

    public String getSecShowMyLink ()
    {
        return secShowMyLink;
    }

    public void setSecShowMyLink (String secShowMyLink)
    {
        this.secShowMyLink = secShowMyLink;
    }

    public String getSecShowUsername ()
    {
        return secShowUsername;
    }

    public void setSecShowUsername (String secShowUsername)
    {
        this.secShowUsername = secShowUsername;
    }

    public String getSecShowEmail ()
    {
        return secShowEmail;
    }

    public void setSecShowEmail (String secShowEmail)
    {
        this.secShowEmail = secShowEmail;
    }

    public String getSecShowFamily ()
    {
        return secShowFamily;
    }

    public void setSecShowFamily (String secShowFamily)
    {
        this.secShowFamily = secShowFamily;
    }

    public String getSecFindMe ()
    {
        return secFindMe;
    }

    public void setSecFindMe (String secFindMe)
    {
        this.secFindMe = secFindMe;
    }

    public String getSecFindMeByPhone ()
    {
        return secFindMeByPhone;
    }

    public void setSecFindMeByPhone (String secFindMeByPhone)
    {
        this.secFindMeByPhone = secFindMeByPhone;
    }

    public String getSecShowGender ()
    {
        return secShowGender;
    }

    public void setSecShowGender (String secShowGender)
    {
        this.secShowGender = secShowGender;
    }
}
