package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked;

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
import javax.persistence.OneToOne;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.time.LocalDateTime;

@Entity
@Table (name = "user_blocked")
public final class UserBlocked
{

    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @OneToOne
    @JoinColumn (name = "id_user_blocked", referencedColumnName = "id")
    private MainAccount mainAccountBlocked;

    @Column (name = "blocked_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime blockedAt;

    @Column (name = "updated_at", insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column (name = "validity_time")
    private LocalDateTime validityTime;

    @Column (name = "unblocked_at", insertable = false)
    private LocalDateTime unblockedAt;

    private boolean unblocked;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private Type type = Type.all;

    public UserBlocked ()
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

    public MainAccount getMainAccountBlocked ()
    {
        return mainAccountBlocked;
    }

    public void setMainAccountBlocked (MainAccount mainAccountBlocked)
    {
        this.mainAccountBlocked = mainAccountBlocked;
    }

    public LocalDateTime getBlockedAt ()
    {
        return blockedAt;
    }

    public void setBlockedAt (LocalDateTime blockedAt)
    {
        this.blockedAt = blockedAt;
    }

    public LocalDateTime getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getValidityTime ()
    {
        return validityTime;
    }

    public void setValidityTime (LocalDateTime validityTime)
    {
        this.validityTime = validityTime;
    }

    public LocalDateTime getUnblockedAt ()
    {
        return unblockedAt;
    }

    public void setUnblockedAt (LocalDateTime unblockedAt)
    {
        this.unblockedAt = unblockedAt;
    }

    public boolean isUnblocked ()
    {
        return unblocked;
    }

    public void setUnblocked (boolean unblocked)
    {
        this.unblocked = unblocked;
    }

    public Type getType ()
    {
        return type;
    }

    public void setType (Type type)
    {
        this.type = type;
    }

    public enum Type
    {
//        cns => Can Not Show

        cns_cover, cns_send_message, cns_profile,
        cns_bio, cns_email, cns_phone, cns_name, cns_family, cns_username, cns_mylink, all
    }

}
