package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.time.LocalDateTime;

@Entity
@Table (name = "users_friends")
public class UserFriends
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @OneToOne
    @JoinColumn (name = "id_user_friend", referencedColumnName = "id")
    private MainAccount mainAccountFriend;

    @Enumerated (EnumType.STRING)
    public StatusFriends status;

    @Column (name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    public LocalDateTime createdAt;

    @Column (name = "updated_at", insertable = false)
    @UpdateTimestamp
    public LocalDateTime updatedAt;

    @Column (name = "deleted_at", insertable = false)
    public LocalDateTime deletedAt;

    public UserFriends ()
    {
    }

    public UserFriends (MainAccount mainAccount , MainAccount mainAccountFriend)
    {
        this.mainAccount = mainAccount;
        this.mainAccountFriend = mainAccountFriend;
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

    public MainAccount getMainAccountFriend ()
    {
        return mainAccountFriend;
    }

    public void setMainAccountFriend (MainAccount mainAccountFriend)
    {
        this.mainAccountFriend = mainAccountFriend;
    }

    public StatusFriends getStatus ()
    {
        return status;
    }

    public void setStatus (StatusFriends status)
    {
        this.status = status;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt ()
    {
        return deletedAt;
    }

    public void setDeletedAt (LocalDateTime deletedAt)
    {
        this.deletedAt = deletedAt;
    }
}
