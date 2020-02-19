package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagementAccessLevel.GroupManagementAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@Table (name = "group_management")
public final class GroupManagement
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    public long id;

    @ManyToOne
    @JoinColumn (name = "id_admin", referencedColumnName = "id")
    private MainAccount mainAccount;

    @ManyToOne
    @JoinColumn (name = "managed_by", referencedColumnName = "id")
    private MainAccount managedBy;

    @ManyToOne
    @JoinColumn (name = "id_group", referencedColumnName = "id")
    private Groups groups;

    @OneToOne
    @JoinColumn (name = "id_access_level", referencedColumnName = "id")
    private GroupManagementAccessLevel accessLevel;

    @Column (name = "management_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime managementAt;

    private String crown;

    private String name;

    @ManyToOne
    @JoinColumn (name = "id_suspended_by", referencedColumnName = "id", insertable = false)
    private MainAccount suspendedBy;

    @Column (name = "suspended_at", insertable = false)
    private LocalDateTime suspendedAt;

    private boolean suspended;

    public GroupManagement ()
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

    public MainAccount getMainAccount ()
    {
        return mainAccount;
    }

    public void setMainAccount (MainAccount mainAccount)
    {
        this.mainAccount = mainAccount;
    }

    public MainAccount getManagedBy ()
    {
        return managedBy;
    }

    public void setManagedBy (MainAccount managedBy)
    {
        this.managedBy = managedBy;
    }

    public Groups getGroups ()
    {
        return groups;
    }

    public void setGroups (Groups groups)
    {
        this.groups = groups;
    }

    public GroupManagementAccessLevel getAccessLevel ()
    {
        return accessLevel;
    }

    public void setAccessLevel (GroupManagementAccessLevel accessLevel)
    {
        this.accessLevel = accessLevel;
    }

    public LocalDateTime getManagementAt ()
    {
        return managementAt;
    }

    public void setManagementAt (LocalDateTime managementAt)
    {
        this.managementAt = managementAt;
    }


    public String getCrown ()
    {
        return crown;
    }

    public void setCrown (String crown)
    {
        this.crown = crown;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public MainAccount getSuspendedBy ()
    {
        return suspendedBy;
    }

    public void setSuspendedBy (MainAccount suspendedBy)
    {
        this.suspendedBy = suspendedBy;
    }

    public LocalDateTime getSuspendedAt ()
    {
        return suspendedAt;
    }

    public void setSuspendedAt (LocalDateTime suspendedAt)
    {
        this.suspendedAt = suspendedAt;
    }

    public boolean isSuspended ()
    {
        return suspended;
    }

    public void setSuspended (boolean suspended)
    {
        this.suspended = suspended;
    }
}
