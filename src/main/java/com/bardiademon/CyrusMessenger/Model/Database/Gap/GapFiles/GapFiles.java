package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
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
@Table (name = "gap_files")
public final class GapFiles
{
    @Id
    @GeneratedValue
    private long id;

    private String name;

    @Column (name = "size_file")
    private long size;

    @Column (name = "type_file")
    private GapFilesTypes types;

    @Column (name = "file_code", nullable = false, unique = true, updatable = false)
    private String code;

    @Column (name = "file_extension", updatable = false)
    private String fileExtension;

    @ManyToOne
    @JoinColumn (name = "id_gap", referencedColumnName = "id")
    private Gaps gaps;

    @Column (name = "uploaded_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime uploadedAt;

    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    public GapFiles ()
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

    public long getSize ()
    {
        return size;
    }

    public void setSize (long size)
    {
        this.size = size;
    }

    public GapFilesTypes getTypes ()
    {
        return types;
    }

    public void setTypes (GapFilesTypes types)
    {
        this.types = types;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public Gaps getGaps ()
    {
        return gaps;
    }

    public void setGaps (Gaps gaps)
    {
        this.gaps = gaps;
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

    public String getFileExtension ()
    {
        return fileExtension;
    }

    public void setFileExtension (String fileExtension)
    {
        this.fileExtension = fileExtension;
    }
}
