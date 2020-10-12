package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups.StickerGroups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "sticker_access_level")
public final class StickerAccessLevel
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "sticker_group_id", referencedColumnName = "id")
    private StickerGroups stickerGroups;

    @ManyToOne
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private MainAccount mainAccount;

    @ManyToOne
    @JoinColumn (name = "group_id", referencedColumnName = "id")
    private Groups groups;

    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    @Column (name = "sticker_access_level_type", nullable = false)
    @Enumerated (EnumType.STRING)
    private StickerAccessLevelType type;

    public StickerAccessLevel ()
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

    public StickerGroups getStickerGroups ()
    {
        return stickerGroups;
    }

    public void setStickerGroups (StickerGroups stickerGroups)
    {
        this.stickerGroups = stickerGroups;
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

    public StickerAccessLevelType getType ()
    {
        return type;
    }

    public void setType (StickerAccessLevelType type)
    {
        this.type = type;
    }
}
