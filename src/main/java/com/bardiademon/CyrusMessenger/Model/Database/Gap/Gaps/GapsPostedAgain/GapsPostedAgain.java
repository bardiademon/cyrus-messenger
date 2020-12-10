package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsPostedAgain;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table (name = "gaps_posted_again")
public final class GapsPostedAgain
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "gap_id", referencedColumnName = "id")
    private Gaps gap;

    @ManyToOne
    @JoinColumn (name = "to_user_id", referencedColumnName = "id")
    private MainAccount to;

    @ManyToOne
    @JoinColumn (name = "message_from", referencedColumnName = "id")
    private MainAccount messageFrom;

    @Column (name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    private boolean deleted;

    public GapsPostedAgain ()
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

    public Gaps getGap ()
    {
        return gap;
    }

    public void setGap (Gaps gap)
    {
        this.gap = gap;
    }

    public MainAccount getTo ()
    {
        return to;
    }

    public void setTo (MainAccount to)
    {
        this.to = to;
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

    public MainAccount getMessageFrom ()
    {
        return messageFrom;
    }

    public void setMessageFrom (MainAccount messageFrom)
    {
        this.messageFrom = messageFrom;
    }
}
