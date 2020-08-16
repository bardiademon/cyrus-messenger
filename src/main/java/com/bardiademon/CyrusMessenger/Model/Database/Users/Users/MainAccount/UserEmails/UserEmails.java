package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfiles;
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
@Table (name = "user_emails")
public final class UserEmails
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @OneToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @OneToOne
    @JoinColumn (name = "id_user_sep_pro", referencedColumnName = "id")
    private UserSeparateProfiles userSeparateProfiles;

    @Column (nullable = false)
    private String email;

    @Column (name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private boolean deleted;

    @Column (name = "deleted_at")
    private LocalDateTime deletedAt;

    private boolean confirmed;

    @Column (name = "confirmed_at", insertable = false)
    private LocalDateTime confirmedAt;

    @Column (name = "email_for", nullable = false)
    @Enumerated (EnumType.STRING)
    private EmailFor emailFor;

    public UserEmails ()
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

    public UserSeparateProfiles getUserSeparateProfiles ()
    {
        return userSeparateProfiles;
    }

    public void setUserSeparateProfiles (UserSeparateProfiles userSeparateProfiles)
    {
        this.userSeparateProfiles = userSeparateProfiles;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public boolean isDeleted ()
    {
        return deleted;
    }

    public void setDeleted (boolean deleted)
    {
        this.deleted = deleted;
    }

    public LocalDateTime getDeletedAt ()
    {
        return deletedAt;
    }

    public void setDeletedAt (LocalDateTime deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public boolean isConfirmed ()
    {
        return confirmed;
    }

    public void setConfirmed (boolean confirmed)
    {
        this.confirmed = confirmed;
    }

    public LocalDateTime getConfirmedAt ()
    {
        return confirmedAt;
    }

    public void setConfirmedAt (LocalDateTime confirmedAt)
    {
        this.confirmedAt = confirmedAt;
    }

    public EmailFor getEmailFor ()
    {
        return emailFor;
    }

    public void setEmailFor (EmailFor emailFor)
    {
        this.emailFor = emailFor;
    }
}
