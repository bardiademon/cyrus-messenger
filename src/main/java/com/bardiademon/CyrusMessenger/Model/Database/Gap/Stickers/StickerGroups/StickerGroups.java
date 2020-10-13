package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.Stickers;
import com.bardiademon.CyrusMessenger.Model.Database.Images.Images;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table (name = "sticker_groups")
public final class StickerGroups
{
    /**
     * TBNAME => table name
     */
    @Transient
    public static final String TBNAME = "sticker_groups";

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @Column (name = "group_name", nullable = false)
    private String groupName;

    @OneToOne
    @JoinColumn (name = "group_image", referencedColumnName = "id")
    private Images groupImage;

    private String description;

    @OneToMany (mappedBy = "group")
    private List <Stickers> stickers;

    @ManyToOne
    @JoinColumn (name = "added_by", nullable = false, referencedColumnName = "id")
    private MainAccount addedBy;

    @Column (name = "added_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime addedAt;

    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    @Column (name = "with_permission", nullable = false)
    private boolean withPermission = false;

    public StickerGroups ()
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

    public String getGroupName ()
    {
        return groupName;
    }

    public void setGroupName (String groupName)
    {
        this.groupName = groupName;
    }

    public Images getGroupImage ()
    {
        return groupImage;
    }

    public void setGroupImage (Images groupImage)
    {
        this.groupImage = groupImage;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public List <Stickers> getStickers ()
    {
        return stickers;
    }

    public void setStickers (List <Stickers> stickers)
    {
        this.stickers = stickers;
    }

    public MainAccount getAddedBy ()
    {
        return addedBy;
    }

    public void setAddedBy (MainAccount addedBy)
    {
        this.addedBy = addedBy;
    }

    public LocalDateTime getAddedAt ()
    {
        return addedAt;
    }

    public void setAddedAt (LocalDateTime addedAt)
    {
        this.addedAt = addedAt;
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

    public boolean isWithPermission ()
    {
        return withPermission;
    }

    public void setWithPermission (boolean withPermission)
    {
        this.withPermission = withPermission;
    }
}
