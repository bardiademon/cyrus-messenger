package com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

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
@Table (name = "blocked_by_the_system")
public final class BlockedByTheSystem
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    private String ip;

    @Column (name = "blocked_for", nullable = false)
    @Enumerated (EnumType.STRING)
    private BlockedFor blockedFor;

    @Column (name = "blocked_description")
    private String description;

    @Column (name = "blocked_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime blockedAt;

    @Column (name = "unblocked_at", insertable = false)
    private LocalDateTime unBlockedAt = null;

    @Column (name = "validity_time")
    @JsonIgnore
    private LocalDateTime validityTime;

    private boolean active = true;

    public BlockedByTheSystem ()
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

    public BlockedFor getBlockedFor ()
    {
        return blockedFor;
    }

    public void setBlockedFor (BlockedFor blockedFor)
    {
        this.blockedFor = blockedFor;
    }

    public LocalDateTime getBlockedAt ()
    {
        return blockedAt;
    }

    public void setBlockedAt (LocalDateTime blockedAt)
    {
        this.blockedAt = blockedAt;
    }

    public LocalDateTime getUnBlockedAt ()
    {
        return unBlockedAt;
    }

    public void setUnBlockedAt (LocalDateTime unBlockedAt)
    {
        this.unBlockedAt = unBlockedAt;
    }

    public LocalDateTime getValidityTime ()
    {
        return validityTime;
    }

    public void setValidityTime (LocalDateTime validityTime)
    {
        this.validityTime = validityTime;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getIp ()
    {
        return ip;
    }

    public void setIp (String ip)
    {
        this.ip = ip;
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
