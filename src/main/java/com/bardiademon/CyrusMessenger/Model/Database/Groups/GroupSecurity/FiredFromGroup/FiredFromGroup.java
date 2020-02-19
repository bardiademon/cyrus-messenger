package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.FiredFromGroup;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Table (name = "fired_from_group")
public final class FiredFromGroup
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_group", referencedColumnName = "id")
    private Groups group;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @ManyToOne
    @JoinColumn (name = "id_fired_by", referencedColumnName = "id")
    private MainAccount firedBy;

    @Column (nullable = false, name = "fired_why")
    private String why;

    @Column (name = "fired_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime firedAt;

    @Column (name = "validity_time", updatable = false, nullable = false)
    private LocalDateTime validityTime;

    @Column (name = "freed_at", insertable = false)
    private LocalDateTime freedAt;

    private boolean freed;

    public FiredFromGroup ()
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

    public Groups getGroup ()
    {
        return group;
    }

    public void setGroup (Groups group)
    {
        this.group = group;
    }

    public MainAccount getMainAccount ()
    {
        return mainAccount;
    }

    public void setMainAccount (MainAccount mainAccount)
    {
        this.mainAccount = mainAccount;
    }

    public MainAccount getFiredBy ()
    {
        return firedBy;
    }

    public void setFiredBy (MainAccount firedBy)
    {
        this.firedBy = firedBy;
    }

    public String getWhy ()
    {
        return why;
    }

    public void setWhy (String why)
    {
        this.why = why;
    }

    public LocalDateTime getFiredAt ()
    {
        return firedAt;
    }

    public void setFiredAt (LocalDateTime firedAt)
    {
        this.firedAt = firedAt;
    }

    public LocalDateTime getValidityTime ()
    {
        return validityTime;
    }

    public void setValidityTime (LocalDateTime validityTime)
    {
        this.validityTime = validityTime;
    }

    public LocalDateTime getFreedAt ()
    {
        return freedAt;
    }

    public void setFreedAt (LocalDateTime freedAt)
    {
        this.freedAt = freedAt;
    }

    public boolean isFreed ()
    {
        return freed;
    }

    public void setFreed (boolean freed)
    {
        this.freed = freed;
    }
}
