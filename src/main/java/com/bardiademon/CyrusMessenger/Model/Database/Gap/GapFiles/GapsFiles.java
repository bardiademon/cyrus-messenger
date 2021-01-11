package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFilesSecurity.GapsFilesSecurity;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.SendGapsFilesTo.SendGapsFilesTo;
import com.bardiademon.CyrusMessenger.Model.Database.UploadedFiles.UploadedFiles;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name = "gap_files")
public final class GapsFiles
{
    @Id
    @GeneratedValue
    private long id;

    @Column (name = "file_code", nullable = false, unique = true, updatable = false)
    private String code;

    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    @OneToOne
    @JoinColumn (name = "uploaded_file", referencedColumnName = "id")
    private UploadedFiles uploadedFiles;

    @Enumerated (EnumType.STRING)
    @Column (name = "gap_file_type", nullable = false)
    private GapFilesTypes type;

    @OneToOne (mappedBy = "gapsFiles")
    private GapsFilesSecurity security;

    @OneToMany (mappedBy = "gapsFiles")
    private List <SendGapsFilesTo> sendGapsFilesTo;

    public GapsFiles ()
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

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
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

    public UploadedFiles getUploadedFiles ()
    {
        return uploadedFiles;
    }

    public void setUploadedFiles (UploadedFiles uploadedFiles)
    {
        this.uploadedFiles = uploadedFiles;
    }

    public GapFilesTypes getType ()
    {
        return type;
    }

    public void setType (GapFilesTypes type)
    {
        this.type = type;
    }

    public GapsFilesSecurity getSecurity ()
    {
        return security;
    }

    public void setSecurity (GapsFilesSecurity security)
    {
        this.security = security;
    }

    public List <SendGapsFilesTo> getSendGapsFilesTo ()
    {
        return sendGapsFilesTo;
    }

    public void setSendGapsFilesTo (List <SendGapsFilesTo> sendGapsFilesTo)
    {
        this.sendGapsFilesTo = sendGapsFilesTo;
    }
}

