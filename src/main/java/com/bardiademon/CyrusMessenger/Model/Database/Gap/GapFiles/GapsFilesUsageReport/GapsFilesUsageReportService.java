package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFilesUsageReport;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.SendGapsFilesTo.SendGapsFilesToService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GapsFilesUsageReportService
{
    public final GapsFilesUsageReportRepository Repository;
    private final SendGapsFilesToService sendGapsFilesToService;

    @Autowired
    public GapsFilesUsageReportService (final GapsFilesUsageReportRepository Repository , final SendGapsFilesToService _SendGapsFilesToService)
    {
        this.Repository = Repository;
        this.sendGapsFilesToService = _SendGapsFilesToService;
    }

    public void used (final GapsFiles gapsFiles , final MainAccount mainAccount , final WhatDidDo whatDidDo)
    {
        final GapsFilesUsageReport gapsFilesUsageReport = new GapsFilesUsageReport ();
        gapsFilesUsageReport.setGapsFiles (gapsFiles);
        gapsFilesUsageReport.setMainAccount (mainAccount);
        gapsFilesUsageReport.setWhatDidDo (whatDidDo);
        Repository.save (gapsFilesUsageReport);

        if (whatDidDo.equals (WhatDidDo.get_link))
            sendGapsFilesToService.send (mainAccount , gapsFiles);
    }
}
