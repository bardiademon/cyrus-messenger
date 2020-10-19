package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups.StickerGroups;
import com.bardiademon.CyrusMessenger.Model.Database.Images.Images;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "gap_stickers")
public final class Stickers
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @Column (name = "sticker_name", length = 100)
    private String name;

    @ManyToOne
    @JoinColumn (name = "sticker_image", referencedColumnName = "id")
    private Images stickerImage;

    @ManyToOne
    @JoinColumn (name = "id_group", referencedColumnName = "id")
    private StickerGroups group;

    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    public Stickers ()
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

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Images getStickerImage ()
    {
        return stickerImage;
    }

    public void setStickerImage (Images stickerImage)
    {
        this.stickerImage = stickerImage;
    }

    public StickerGroups getGroup ()
    {
        return group;
    }

    public void setGroup (StickerGroups group)
    {
        this.group = group;
    }

    public boolean getDeleted ()
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
