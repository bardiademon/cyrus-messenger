package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup;

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
public final class JoinGroup
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

    @Column (name = "join_by", nullable = false)
    @Enumerated (EnumType.STRING)
    private JoinBy joinBy;

    // If JoinFor == user
    @ManyToOne
    @JoinColumn (name = "added_by", referencedColumnName = "id", updatable = false)
    private MainAccount addedBy;

    @Column (name = "time_join", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime timeJoin;

    @Column (name = "time_leave", insertable = false)
    @UpdateTimestamp
    private LocalDateTime timeLeave;

    @Column (name = "leave_group")
    private boolean leaveGroup = false;

    public JoinGroup ()
    {
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

    public MainAccount getAddedBy ()
    {
        return addedBy;
    }

    public void setAddedBy (MainAccount addedBy)
    {
        this.addedBy = addedBy;
    }

    public JoinBy getJoinBy ()
    {
        return joinBy;
    }

    public void setJoinBy (JoinBy joinFor)
    {
        this.joinBy = joinFor;
    }

    public boolean isLeaveGroup ()
    {
        return leaveGroup;
    }

    public void setLeaveGroup (boolean leaveGroup)
    {
        this.leaveGroup = leaveGroup;
    }

    public enum JoinBy
    {
        user, the_user_himself
    }
}
