package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFilesSecurity;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name = "security_gaps_files")
public final class GapsFilesSecurity
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @OneToOne
    @JoinColumn (name = "gap_file", referencedColumnName = "id")
    private GapsFiles gapsFiles;

    @Column (name = "can_forward", nullable = false)
    private boolean canForward = true;

    @Column (name = "can_download", nullable = false)
    private boolean canDownload = true;

    @Column (name = "can_send_to_groups", nullable = false)
    private boolean canSendToGroups = true;

    @Column (name = "can_only_see_once", nullable = false)
    private boolean justOnce = false;

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

    public boolean isCanForward ()
    {
        return canForward;
    }

    public void setCanForward (boolean canForward)
    {
        this.canForward = canForward;
    }

    public boolean isCanDownload ()
    {
        return canDownload;
    }

    public void setCanDownload (boolean canDownload)
    {
        this.canDownload = canDownload;
    }

    public boolean isCanSendToGroups ()
    {
        return canSendToGroups;
    }

    public void setCanSendToGroups (boolean canSendToGroups)
    {
        this.canSendToGroups = canSendToGroups;
    }

    public boolean isJustOnce ()
    {
        return justOnce;
    }

    public void setJustOnce (boolean justOnce)
    {
        this.justOnce = justOnce;
    }
}
