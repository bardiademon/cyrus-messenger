package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps;

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
@Table (name = "personal_gap")
public final class PersonalGaps
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "created_by", updatable = false, referencedColumnName = "id")
    private MainAccount createdBy;

    @ManyToOne
    @JoinColumn (name = "gap_with", updatable = false, referencedColumnName = "id")
    private MainAccount gapWith;

    @Column (name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column (name = "last_index")
    private long lastIndex;

    @Column (name = "deleted_for_created_by")
    private boolean deletedByCreatedBy;

    @Column (name = "deleted_for_gap_with")
    private boolean deletedForGapWith;

    @Column (name = "deleted_at_created_by", insertable = false)
    private LocalDateTime deletedAt_CreatedBy;

    @Column (name = "deleted_at_gap_with", insertable = false)
    private LocalDateTime deletedAt_GapWith;

    @Column (name = "last_message")
    private LocalDateTime lastMessage;

    @ManyToOne
    @JoinColumn (name = "delete_both_by", referencedColumnName = "id")
    private MainAccount deleteBothBy;

    public PersonalGaps ()
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

    public MainAccount getCreatedBy ()
    {
        return createdBy;
    }

    public void setCreatedBy (MainAccount createdBy)
    {
        this.createdBy = createdBy;
    }

    public MainAccount getGapWith ()
    {
        return gapWith;
    }

    public void setGapWith (MainAccount gapWith)
    {
        this.gapWith = gapWith;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public boolean isDeletedByCreatedBy ()
    {
        return deletedByCreatedBy;
    }

    public void setDeletedByCreatedBy (boolean deletedByCreatedBy)
    {
        this.deletedByCreatedBy = deletedByCreatedBy;
    }

    public boolean isDeletedForGapWith ()
    {
        return deletedForGapWith;
    }

    public void setDeletedForGapWith (boolean deletedForGapWith)
    {
        this.deletedForGapWith = deletedForGapWith;
    }

    public LocalDateTime getDeletedAt_CreatedBy ()
    {
        return deletedAt_CreatedBy;
    }

    public void setDeletedAt_CreatedBy (LocalDateTime deletedAt_CreatedBy)
    {
        this.deletedAt_CreatedBy = deletedAt_CreatedBy;
    }

    public LocalDateTime getDeletedAt_GapWith ()
    {
        return deletedAt_GapWith;
    }

    public void setDeletedAt_GapWith (LocalDateTime deletedAt_GapWith)
    {
        this.deletedAt_GapWith = deletedAt_GapWith;
    }

    public long getLastIndex ()
    {
        return lastIndex;
    }

    public void setLastIndex (long lastIndex)
    {
        this.lastIndex = lastIndex;
    }

    public LocalDateTime getLastMessage ()
    {
        return lastMessage;
    }

    public void setLastMessage (LocalDateTime lastMessage)
    {
        this.lastMessage = lastMessage;
    }

    public MainAccount getDeleteBothBy ()
    {
        return deleteBothBy;
    }

    public void setDeleteBothBy (MainAccount deleteBothBy)
    {
        this.deleteBothBy = deleteBothBy;
    }
}
