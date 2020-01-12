package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor;

import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChat;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

@Entity
@Table (name = "show_profile_for")
public class ShowProfileFor
{
    public final static String IsolationWith = ",";

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @OneToOne
    @JoinColumn (name = "id_security_profile", referencedColumnName = "id")
    private SecurityUserProfile securityUserProfile;

    @OneToOne
    @JoinColumn (name = "id_security_chat", referencedColumnName = "id")
    private SecurityUserChat securityUserChat;

    @Enumerated (value = EnumType.STRING)
    @Column (nullable = false)
    private AccessLevel security;

    @Column (name = "show_just")
    private String showJust;

    @Column (name = "list_just_friends")
    private String showJustFriends;

    @Column (name = "list_all_except")
    private String showAllExcept;

    public ShowProfileFor ()
    {
    }

    public ShowProfileFor (SecurityUserProfile securityUserProfile , SecurityUserChat securityUserChat , AccessLevel security , String showJust , String showJustFriends , String showAllExcept)
    {
        this.securityUserProfile = securityUserProfile;
        this.securityUserChat = securityUserChat;
        this.security = security;
        this.showJust = showJust;
        this.showJustFriends = showJustFriends;
        this.showAllExcept = showAllExcept;
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public SecurityUserProfile getSecurityUserProfile ()
    {
        return securityUserProfile;
    }

    public static String getIsolationWith ()
    {
        return IsolationWith;
    }

    public SecurityUserChat getSecurityUserChat ()
    {
        return securityUserChat;
    }

    public void setSecurityUserChat (SecurityUserChat securityUserChat)
    {
        this.securityUserChat = securityUserChat;
    }

    public void setSecurityUserProfile (SecurityUserProfile securityUserProfile)
    {
        this.securityUserProfile = securityUserProfile;
    }

    public AccessLevel getSecurity ()
    {
        return security;
    }

    public void setSecurity (AccessLevel security)
    {
        this.security = security;
    }

    public String getShowJust ()
    {
        return showJust;
    }

    public void setShowJust (String showJust)
    {
        this.showJust = showJust;
    }

    public String getShowJustFriends ()
    {
        return showJustFriends;
    }

    public void setShowJustFriends (String showJustFriends)
    {
        this.showJustFriends = showJustFriends;
    }

    public String getShowAllExcept ()
    {
        return showAllExcept;
    }

    public void setShowAllExcept (String showAllExcept)
    {
        this.showAllExcept = showAllExcept;
    }
}
