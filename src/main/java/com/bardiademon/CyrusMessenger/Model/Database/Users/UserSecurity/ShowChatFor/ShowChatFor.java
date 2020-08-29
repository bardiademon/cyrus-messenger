package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor;

import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserGap.SecurityUserGap;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

@Entity
@Table (name = "show_chat_for")
public class ShowChatFor
{
    public final static String IsolationWith = ",";

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @OneToOne
    @JoinColumn (name = "id_security_chat", referencedColumnName = "id")
    private SecurityUserGap securityUserGap;

    @Column (name = "show_just")
    protected String showJust;

    @Column (name = "list_all_except")
    protected String showAllExcept;

    public ShowChatFor ()
    {
    }

    public ShowChatFor (SecurityUserGap securityUserGap , String showJust , String showJustFriends , String showAllExcept)
    {
        this.securityUserGap = securityUserGap;
        this.showJust = showJust;
        this.showAllExcept = showAllExcept;
    }

    public static String getIsolationWith ()
    {
        return IsolationWith;
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public SecurityUserGap getSecurityUserChat ()
    {
        return securityUserGap;
    }

    public void setSecurityUserChat (SecurityUserGap securityUserGap)
    {
        this.securityUserGap = securityUserGap;
    }

    public String getShowJust ()
    {
        return showJust;
    }

    public void setShowJust (String showJust)
    {
        this.showJust = showJust;
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
