package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups.StickerGroups;
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

    @Column (name = "sticker_name")
    private String name;

    @Column (name = "sticket_image", nullable = false)
    private String stickerImage;

    @ManyToOne
    @JoinColumn (name = "id_group", referencedColumnName = "id")
    private StickerGroups group;

    private LocalDateTime deleted;

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

    public String getStickerImage ()
    {
        return stickerImage;
    }

    public void setStickerImage (String stickerImage)
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

    public LocalDateTime getDeleted ()
    {
        return deleted;
    }

    public void setDeleted (LocalDateTime deleted)
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
