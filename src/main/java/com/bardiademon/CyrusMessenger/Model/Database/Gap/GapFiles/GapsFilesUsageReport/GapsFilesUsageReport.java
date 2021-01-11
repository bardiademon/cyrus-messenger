package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFilesUsageReport;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table (name = "Gaps_files_usage_report")
public final class GapsFilesUsageReport
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private MainAccount mainAccount;

    @Enumerated (EnumType.STRING)
    @Column (name = "what_did_do", nullable = false)
    private WhatDidDo whatDidDo;

    @ManyToOne
    @JoinColumn (name = "gap_file_id")
    private GapsFiles gapsFiles;

    @Column (name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public GapsFilesUsageReport ()
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

    public WhatDidDo getWhatDidDo ()
    {
        return whatDidDo;
    }

    public void setWhatDidDo (WhatDidDo whatDidDo)
    {
        this.whatDidDo = whatDidDo;
    }

    public GapsFiles getGapsFiles ()
    {
        return gapsFiles;
    }

    public void setGapsFiles (GapsFiles gapsFiles)
    {
        this.gapsFiles = gapsFiles;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}
