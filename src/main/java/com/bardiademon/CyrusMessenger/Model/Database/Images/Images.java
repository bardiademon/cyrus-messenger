package com.bardiademon.CyrusMessenger.Model.Database.Images;

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
@Table (name = "uploaded_images")
public final class Images
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @Column (name = "img_name", nullable = false)
    private String name;

    @Column (name = "img_type", nullable = false)
    private String type;

    @Column (name = "img_size", nullable = false)
    private long size;

    @Column (name = "img_width", nullable = false)
    private int width;

    @Column (name = "img_height", nullable = false)
    private int height;

    @Column (name = "saved_path", nullable = false)
    private String savedPath;

    @Column (name = "image_for", nullable = false)
    private String imageFor;

    @Column (name = "uploaded_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime uploadedAt;

    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn (name = "uploaded_by", referencedColumnName = "id")
    private MainAccount uploadedBy;

    public Images ()
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

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public long getSize ()
    {
        return size;
    }

    public void setSize (long size)
    {
        this.size = size;
    }

    public int getWidth ()
    {
        return width;
    }

    public void setWidth (int width)
    {
        this.width = width;
    }

    public int getHeight ()
    {
        return height;
    }

    public void setHeight (int height)
    {
        this.height = height;
    }

    public String getSavedPath ()
    {
        return savedPath;
    }

    public void setSavedPath (String savedPath)
    {
        this.savedPath = savedPath;
    }

    public String getImageFor ()
    {
        return imageFor;
    }

    public void setImageFor (String imageFor)
    {
        this.imageFor = imageFor;
    }

    public LocalDateTime getUploadedAt ()
    {
        return uploadedAt;
    }

    public void setUploadedAt (LocalDateTime uploadedAt)
    {
        this.uploadedAt = uploadedAt;
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

    public MainAccount getUploadedBy ()
    {
        return uploadedBy;
    }

    public void setUploadedBy (MainAccount uploadedBy)
    {
        this.uploadedBy = uploadedBy;
    }
}
