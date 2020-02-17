package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagement;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Table (name = "list_join_group")
public class JoinGroup
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_group", referencedColumnName = "id")
    private Groups groups;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @Column (name = "join_for", nullable = false)
    @Enumerated (EnumType.STRING)
    private JoinFor joinFor;

    // LU => Link or username
    @Column (name = "join_for_lu", updatable = false)
    private String joinForLU;

    // If JoinFor == user
    @ManyToOne
    @JoinColumn (name = "added_by", referencedColumnName = "id", updatable = false)
    private MainAccount addedBy;

    @Column (name = "time_join", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime timeJoin;

    @Column (name = "time_leave", nullable = false, insertable = false)
    @UpdateTimestamp
    private LocalDateTime timeLeave;

    @Column (name = "fired", insertable = false)
    private boolean fired = false;

    @ManyToOne
    @JoinColumn (name = "fired_by", referencedColumnName = "id", insertable = false)
    private GroupManagement firedBy;

    public JoinGroup ()
    {
    }

    public JoinGroup (Groups groups , MainAccount mainAccount , LocalDateTime timeJoin , LocalDateTime timeLeave)
    {
        this.groups = groups;
        this.mainAccount = mainAccount;
        this.timeJoin = timeJoin;
        this.timeLeave = timeLeave;
    }


    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public Groups getGroups ()
    {
        return groups;
    }

    public void setGroups (Groups groups)
    {
        this.groups = groups;
    }

    public MainAccount getMainAccount ()
    {
        return mainAccount;
    }

    public void setMainAccount (MainAccount mainAccount)
    {
        this.mainAccount = mainAccount;
    }

    public LocalDateTime getTimeJoin ()
    {
        return timeJoin;
    }

    public void setTimeJoin (LocalDateTime timeJoin)
    {
        this.timeJoin = timeJoin;
    }

    public LocalDateTime getTimeLeave ()
    {
        return timeLeave;
    }

    public void setTimeLeave (LocalDateTime timeLeave)
    {
        this.timeLeave = timeLeave;
    }

    public boolean isFired ()
    {
        return fired;
    }

    public void setFired (boolean fired)
    {
        this.fired = fired;
    }

    public JoinFor getJoinFor ()
    {
        return joinFor;
    }

    public void setJoinFor (JoinFor joinFor)
    {
        this.joinFor = joinFor;
    }

    public String getJoinForLU ()
    {
        return joinForLU;
    }

    public void setJoinForLU (String joinForLU)
    {
        this.joinForLU = joinForLU;
    }

    private enum JoinFor
    {
        username, link, user
    }
}
