package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Get.Security.Profile;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestProfile
{
//    sec => Security

    @JsonProperty ("sec_cover")
    private boolean secCover;

    @JsonProperty ("sec_bio")
    private boolean secBio;

    @JsonProperty ("sec_show_in_channel")
    private boolean secShowInChannel;

    @JsonProperty ("sec_show_in_group")
    private boolean secShowInGroup;

    @JsonProperty ("sec_show_profile")
    private boolean secShowProfile;

    @JsonProperty ("sec_show_in_search")
    private boolean secShowInSearch;

    @JsonProperty ("sec_last_seen")
    private boolean secLastSeen;

    @JsonProperty ("sec_mylink")
    private boolean secMyLink;

    @JsonProperty ("sec_name")
    private boolean secName;

    @JsonProperty ("sec_personal_information")
    private boolean secPersonalInformation;

    @JsonProperty ("sec_phone")
    private boolean secPhone;

    @JsonProperty ("sec_seen_message")
    private boolean secSeenMessage;

    @JsonProperty ("sec_username")
    private boolean secUsername;

    @JsonProperty ("sec_list_friends")
    private boolean secListFriends;

    public RequestProfile ()
    {
    }

    public boolean isSecCover ()
    {
        return secCover;
    }

    public void setSecCover (boolean secCover)
    {
        this.secCover = secCover;
    }

    public boolean isSecBio ()
    {
        return secBio;
    }

    public void setSecBio (boolean secBio)
    {
        this.secBio = secBio;
    }

    public boolean isSecShowInChannel ()
    {
        return secShowInChannel;
    }

    public void setSecShowInChannel (boolean secShowInChannel)
    {
        this.secShowInChannel = secShowInChannel;
    }

    public boolean isSecShowInGroup ()
    {
        return secShowInGroup;
    }

    public void setSecShowInGroup (boolean secShowInGroup)
    {
        this.secShowInGroup = secShowInGroup;
    }

    public boolean isSecShowProfile ()
    {
        return secShowProfile;
    }

    public void setSecShowProfile (boolean secShowProfile)
    {
        this.secShowProfile = secShowProfile;
    }

    public boolean isSecLastSeen ()
    {
        return secLastSeen;
    }

    public void setSecLastSeen (boolean secLastSeen)
    {
        this.secLastSeen = secLastSeen;
    }

    public boolean isSecMyLink ()
    {
        return secMyLink;
    }

    public void setSecMyLink (boolean secMyLink)
    {
        this.secMyLink = secMyLink;
    }

    public boolean isSecName ()
    {
        return secName;
    }

    public void setSecName (boolean secName)
    {
        this.secName = secName;
    }

    public boolean isSecPersonalInformation ()
    {
        return secPersonalInformation;
    }

    public void setSecPersonalInformation (boolean secPersonalInformation)
    {
        this.secPersonalInformation = secPersonalInformation;
    }

    public boolean isSecPhone ()
    {
        return secPhone;
    }

    public void setSecPhone (boolean secPhone)
    {
        this.secPhone = secPhone;
    }

    public boolean isSecSeenMessage ()
    {
        return secSeenMessage;
    }

    public void setSecSeenMessage (boolean secSeenMessage)
    {
        this.secSeenMessage = secSeenMessage;
    }

    public boolean isSecUsername ()
    {
        return secUsername;
    }

    public void setSecUsername (boolean secUsername)
    {
        this.secUsername = secUsername;
    }

    public boolean isSecShowInSearch ()
    {
        return secShowInSearch;
    }

    public void setSecShowInSearch (boolean secShowInSearch)
    {
        this.secShowInSearch = secShowInSearch;
    }

    public boolean thereIsAtLeastOneTrue ()
    {
        return (
                isSecBio () ||
                        isSecCover () ||
                        isSecLastSeen () ||
                        isSecMyLink () ||
                        isSecName () ||
                        isSecPersonalInformation () ||
                        isSecPhone () ||
                        isSecShowInChannel () ||
                        isSecSeenMessage () ||
                        isSecShowInGroup () ||
                        isSecShowProfile () ||
                        isSecUsername () ||
                        isSecShowInSearch () || isSecListFriends ()
        );
    }

    public boolean isSecListFriends ()
    {
        return secListFriends;
    }

    public void setSecListFriends (boolean secListFriends)
    {
        this.secListFriends = secListFriends;
    }
}
