package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EnumType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;


@Entity
@Table (name = "user_list")
public final class UserList
{

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    /**
     * kasi ke ezafe karde
     */
    @ManyToOne
    @JoinColumn (name = "id_user_1", referencedColumnName = "id")
    private MainAccount mainAccount;

    /**
     * kasy ke ezafe shode
     */
    @ManyToOne
    @JoinColumn (name = "id_user_2", referencedColumnName = "id")
    private MainAccount user;

    @Column (name = "user_list_type", nullable = false)
    @Enumerated (EnumType.STRING)
    private UserListType type;

    @Column (name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    private boolean deleted;

    public UserList ()
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

    public MainAccount getUser ()
    {
        return user;
    }

    public void setUser (MainAccount user)
    {
        this.user = user;
    }

    public UserListType getType ()
    {
        return type;
    }

    public void setType (UserListType type)
    {
        this.type = type;
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
}
