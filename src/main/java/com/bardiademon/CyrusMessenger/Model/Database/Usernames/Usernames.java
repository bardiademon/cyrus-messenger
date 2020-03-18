package com.bardiademon.CyrusMessenger.Model.Database.Usernames;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Table (name = "usernames")
public final class Usernames
{

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    private String username;

    @OneToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @OneToOne
    @JoinColumn (name = "id_group", referencedColumnName = "id")
    private Groups groups;

    @Column (name = "username_for", nullable = false)
    @Enumerated (EnumType.STRING)
    private UsernameFor usernameFor;

    @Column (name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    private boolean deleted;

    private boolean active = true;

    public Usernames ()
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

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public MainAccount getMainAccount ()
    {
        return mainAccount;
    }

    public void setMainAccount (MainAccount mainAccount)
    {
        this.mainAccount = mainAccount;
    }

    public Groups getGroups ()
    {
        return groups;
    }

    public void setGroups (Groups groups)
    {
        this.groups = groups;
    }

    public UsernameFor getUsernameFor ()
    {
        return usernameFor;
    }

    public void setUsernameFor (UsernameFor usernameFor)
    {
        this.usernameFor = usernameFor;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeletedAt ()
    {
        return deletedAt;
    }

    public void setDeletedAt (LocalDateTime deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted ()
    {
        return deleted;
    }

    public void setDeleted (boolean deleted)
    {
        this.deleted = deleted;
    }

    public boolean isActive ()
    {
        return active;
    }

    public void setActive (boolean active)
    {
        this.active = active;
    }
}