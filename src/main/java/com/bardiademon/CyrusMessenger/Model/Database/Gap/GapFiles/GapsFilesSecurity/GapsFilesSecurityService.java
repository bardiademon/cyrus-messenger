package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFilesSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GapsFilesSecurityService
{
    public final GapsFilesSecurityRepository Repository;

    @Autowired
    public GapsFilesSecurityService (final GapsFilesSecurityRepository Repository)
    {
        this.Repository = Repository;
    }

}
