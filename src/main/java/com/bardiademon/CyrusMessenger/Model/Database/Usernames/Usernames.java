package com.bardiademon.CyrusMessenger.Model.Database.Usernames;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table (name = "usernames")
public final class Usernames
{

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    @JsonIgnore
    private long id;

    /*
     * baraye hazf
     */
    @JsonIgnore
    private long id2;

    private String username;

    @OneToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    @JsonIgnore
    private MainAccount mainAccount;

    @OneToOne
    @JoinColumn (name = "id_group", referencedColumnName = "id")
    @JsonIgnore
    private Groups groups;

    @Column (name = "username_for", nullable = false)
    @Enumerated (EnumType.STRING)
    @JsonIgnore
    private UsernameFor usernameFor;

    @Column (name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdAt;

    @Column (name = "deleted_at", insertable = false)
    @JsonIgnore
    private LocalDateTime deletedAt;

    @JsonIgnore
    private boolean deleted;

    @JsonIgnore
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

    public long getId2 ()
    {
        return id2;
    }

    public void setId2 (long id2)
    {
        this.id2 = id2;
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
