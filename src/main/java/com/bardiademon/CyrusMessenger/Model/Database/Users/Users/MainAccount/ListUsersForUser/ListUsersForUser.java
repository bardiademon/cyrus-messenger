package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ListUsersForUser;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.time.LocalDateTime;

@Entity
@Table (name = "list_users_for_user")
public class ListUsersForUser
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @ManyToOne
    @JoinColumn (name = "id_user_list", referencedColumnName = "id")
    private MainAccount mainAccountList;

    @Column (name = "user_for", nullable = false)
    @Enumerated (EnumType.STRING)
    private UserFor userFor;

    private boolean deleted = false;

    @Column (name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    public LocalDateTime createdAt;

    @Column (name = "updated_at", insertable = false)
    @UpdateTimestamp
    public LocalDateTime updatedAt;

    @Column (name = "deleted_at", insertable = false)
    public LocalDateTime deletedAt;

    public ListUsersForUser ()
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


    public MainAccount getMainAccountList ()
    {
        return mainAccountList;
    }

    public void setMainAccountList (MainAccount mainAccountList)
    {
        this.mainAccountList = mainAccountList;
    }

    public UserFor getUserFor ()
    {
        return userFor;
    }

    public void setUserFor (UserFor userFor)
    {
        this.userFor = userFor;
    }

    public boolean isDeleted ()
    {
        return deleted;
    }

    public void setDeleted (boolean deleted)
    {
        this.deleted = deleted;
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
