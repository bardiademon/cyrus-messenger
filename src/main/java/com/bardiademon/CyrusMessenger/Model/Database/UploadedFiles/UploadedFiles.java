package com.bardiademon.CyrusMessenger.Model.Database.UploadedFiles;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import java.io.File;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table (name = "uploaded_files")
public final class UploadedFiles
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @Column (name = "file_size", nullable = false)
    private long size;

    @Column (name = "file_name", nullable = false, length = 1000000)
    private String name;

    @Column (name = "file_type", nullable = false, length = 20)
    private String type;

    @Column (name = "saved_path", nullable = false, length = 1000000)
    private String savedPath;

    @ManyToOne
    @JoinColumn (name = "uploaded_by", nullable = false)
    private MainAccount uploadedBy;

    // classi ke dar un save anjam shode
    @Column (name = "file_for", nullable = false, length = 100000)
    private String fileFor;

    @Column (name = "file_content_type", length = 100)
    private String contentType;

    @Column (name = "uploaded_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime uploadedAt;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    private boolean deleted;

    @Column (name = "file_description", length = 100000000)
    private String description;

    // agar image bashad
    @Column (name = "img_width")
    private int width;

    // agar image bashad
    @Column (name = "img_height")
    private int height;

    @Transient
    public File toFile ()
    {
        return new File (Path.StickTogether (getType () , Str.toArray (getSavedPath () , getName ())));
    }

    public UploadedFiles ()
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

    public long getSize ()
    {
        return size;
    }

    public void setSize (long size)
    {
        this.size = size;
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

    public String getSavedPath ()
    {
        return savedPath;
    }

    public void setSavedPath (String savedPath)
    {
        this.savedPath = savedPath;
    }

    public MainAccount getUploadedBy ()
    {
        return uploadedBy;
    }

    public void setUploadedBy (MainAccount uploadedBy)
    {
        this.uploadedBy = uploadedBy;
    }

    public String getFileFor ()
    {
        return fileFor;
    }

    public void setFileFor (String fileFor)
    {
        this.fileFor = fileFor;
    }

    public String getContentType ()
    {
        return contentType;
    }

    public void setContentType (String contentType)
    {
        this.contentType = contentType;
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

    public boolean isDeleted ()
    {
        return deleted;
    }

    public void setDeleted (boolean deleted)
    {
        this.deleted = deleted;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
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
}
