package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups.StickerGroups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
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

    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

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
}
