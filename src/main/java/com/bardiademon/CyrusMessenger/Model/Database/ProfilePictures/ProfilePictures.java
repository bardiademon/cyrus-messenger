package com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Images.Images;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Table (name = "profile_pictures")
public final class ProfilePictures
{
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    @JsonIgnore
    private MainAccount mainAccount;

    @ManyToOne
    @JoinColumn (name = "id_group", referencedColumnName = "id")
    private Groups groups;

    @ManyToOne
    @JoinColumn (name = "image_id", referencedColumnName = "id")
    private Images image;

    private boolean deleted;

    @Column (name = "main_pic", nullable = false)
    private boolean mainPic = false;

    @Column (name = "placement_number", nullable = false)
    private int placementNumber = 0;

    @Column (name = "this_pic_for", nullable = false)
    @Enumerated (EnumType.STRING)
    private ProfilePicFor thisPicFor;

    @Column (name = "uploaded_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime uploadedAt;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    @Column (name = "updated_at", insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean separate = false;

    public ProfilePictures ()
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

    public Groups getGroups ()
    {
        return groups;
    }

    public void setGroups (Groups groups)
    {
        this.groups = groups;
    }

    public Images getImage ()
    {
        return image;
    }

    public void setImage (Images image)
    {
        this.image = image;
    }

    public boolean isDeleted ()
    {
        return deleted;
    }

    public void setDeleted (boolean deleted)
    {
        this.deleted = deleted;
    }

    public boolean isMainPic ()
    {
        return mainPic;
    }

    public void setMainPic (boolean mainPic)
    {
        this.mainPic = mainPic;
    }

    public int getPlacementNumber ()
    {
        return placementNumber;
    }

    public void setPlacementNumber (int placementNumber)
    {
        this.placementNumber = placementNumber;
    }

    public ProfilePicFor getThisPicFor ()
    {
        return thisPicFor;
    }

    public void setThisPicFor (ProfilePicFor thisPicFor)
    {
        this.thisPicFor = thisPicFor;
    }

    public LocalDateTime getUploadedAt ()
    {
        return uploadedAt;
    }

    public void setUploadedAt (LocalDateTime uploadedAt)
    {
        this.uploadedAt = uploadedAt;
    }

    public LocalDateTime getDeletedAt ()
    {
        return deletedAt;
    }

    public void setDeletedAt (LocalDateTime deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public LocalDateTime getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public boolean isSeparate ()
    {
        return separate;
    }

    public void setSeparate (boolean separate)
    {
        this.separate = separate;
    }
}
