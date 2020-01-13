package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor;

import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;

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

    @Column (name = "show_just")
    protected String showJust;

    @Column (name = "list_just_friends")
    protected String showJustFriends;

    @Column (name = "list_all_except")
    protected String showAllExcept;

    public ShowProfileFor ()
    {
    }

    public ShowProfileFor (SecurityUserProfile securityUserProfile , String showJust , String showJustFriends , String showAllExcept)
    {
        this.securityUserProfile = securityUserProfile;
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

    public void setSecurityUserProfile (SecurityUserProfile securityUserProfile)
    {
        this.securityUserProfile = securityUserProfile;
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
