package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFilesUsageReport;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GapsFilesUsageReportService
{
    public final GapsFilesUsageReportRepository Repository;

    @Autowired
    public GapsFilesUsageReportService (final GapsFilesUsageReportRepository Repository)
    {
        this.Repository = Repository;
    }

    public void used (final GapsFiles gapsFiles , final MainAccount mainAccount , final WhatDidDo whatDidDo)
    {
        final GapsFilesUsageReport gapsFilesUsageReport = new GapsFilesUsageReport ();
        gapsFilesUsageReport.setGapsFiles (gapsFiles);
        gapsFilesUsageReport.setMainAccount (mainAccount);
        gapsFilesUsageReport.setWhatDidDo (whatDidDo);
        Repository.save (gapsFilesUsageReport);
    }
}
