package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Get.General;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestGeneral
{
    @JsonProperty ("get_name")
    protected boolean getName;

    @JsonProperty ("get_id")
    protected boolean getId;

    @JsonProperty ("get_family")
    protected boolean getFamily;

    @JsonProperty ("get_username")
    protected boolean getUsername;

    @JsonProperty ("get_email")
    protected boolean getEmail;

    @JsonProperty ("get_phone")
    protected boolean getPhone;

    @JsonProperty ("get_mylink")
    protected boolean getMyLink;

    @JsonProperty ("get_bio")
    protected boolean getBio;

    @JsonProperty ("get_list_friends")
    private boolean getListFriends;

    @JsonProperty ("get_list_friends_reject")
    private boolean getListFriendsReject;

    @JsonProperty ("get_list_friends_awaiting_approval")
    private boolean getListFriendsAwaitingApproval;

    @JsonProperty ("get_list_friends_deleted")
    private boolean getListFriendsDeleted;

    @JsonProperty ("get_gender")
    private boolean getGender;

    public RequestGeneral ()
    {
    }

    public boolean atLeastOne ()
    {
        return (isGetName () || isGetFamily () || isGetUsername () || isGetEmail () || isGetPhone () || isGetMyLink () || isGetBio () || isGetListFriends ());
    }

    public boolean isGetName ()
    {
        return getName;
    }

    public void setGetName (boolean getName)
    {
        this.getName = getName;
    }

    public boolean isGetId ()
    {
        return getId;
    }

    public void setGetId (boolean getId)
    {
        this.getId = getId;
    }

    public boolean isGetFamily ()
    {
        return getFamily;
    }

    public void setGetFamily (boolean getFamily)
    {
        this.getFamily = getFamily;
    }

    public boolean isGetUsername ()
    {
        return getUsername;
    }

    public void setGetUsername (boolean getUsername)
    {
        this.getUsername = getUsername;
    }

    public boolean isGetEmail ()
    {
        return getEmail;
    }

    public void setGetEmail (boolean getEmail)
    {
        this.getEmail = getEmail;
    }

    public boolean isGetPhone ()
    {
        return getPhone;
    }

    public void setGetPhone (boolean getPhone)
    {
        this.getPhone = getPhone;
    }

    public boolean isGetMyLink ()
    {
        return getMyLink;
    }

    public void setGetMyLink (boolean getMyLink)
    {
        this.getMyLink = getMyLink;
    }

    public boolean isGetBio ()
    {
        return getBio;
    }

    public void setGetBio (boolean getBio)
    {
        this.getBio = getBio;
    }

    public boolean isGetListFriends ()
    {
        return getListFriends;
    }

    public void setGetListFriends (boolean getListFriends)
    {
        this.getListFriends = getListFriends;
    }

    public boolean isGetListFriendsReject ()
    {
        return getListFriendsReject;
    }

    public void setGetListFriendsReject (boolean getListFriendsReject)
    {
        this.getListFriendsReject = getListFriendsReject;
    }

    public boolean isGetListFriendsAwaitingApproval ()
    {
        return getListFriendsAwaitingApproval;
    }

    public void setGetListFriendsAwaitingApproval (boolean getListFriendsAwaitingApproval)
    {
        this.getListFriendsAwaitingApproval = getListFriendsAwaitingApproval;
    }

    public boolean isGetListFriendsDeleted ()
    {
        return getListFriendsDeleted;
    }

    public void setGetListFriendsDeleted (boolean getListFriendsDeleted)
    {
        this.getListFriendsDeleted = getListFriendsDeleted;
    }

    public boolean isGetGender ()
    {
        return getGender;
    }

    public void setGetGender (boolean getGender)
    {
        this.getGender = getGender;
    }
}
