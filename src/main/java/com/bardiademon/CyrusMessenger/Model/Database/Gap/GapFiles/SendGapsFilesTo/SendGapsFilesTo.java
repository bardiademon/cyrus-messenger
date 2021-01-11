package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.SendGapsFilesTo;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
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
@Table (name = "send_gaps_files_to")
public final class SendGapsFilesTo
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_gaps_files", referencedColumnName = "id")
    private GapsFiles gapsFiles;

    @ManyToOne
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private MainAccount mainAccount;

    @ManyToOne
    @JoinColumn (name = "group_id", referencedColumnName = "id")
    private Groups groups;

    @Column (name = "send_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime sendAt;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    private boolean deleted;

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public GapsFiles getGapsFiles ()
    {
        return gapsFiles;
    }

    public void setGapsFiles (GapsFiles gapsFiles)
    {
        this.gapsFiles = gapsFiles;
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

    public LocalDateTime getSendAt ()
    {
        return sendAt;
    }

    public void setSendAt (LocalDateTime sendAt)
    {
        this.sendAt = sendAt;
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
}
