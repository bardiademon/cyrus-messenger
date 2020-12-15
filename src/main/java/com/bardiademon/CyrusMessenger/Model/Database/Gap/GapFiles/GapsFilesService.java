package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GapsFilesService
{
    public final GapsFilesRepository Repository;

    @Autowired
    public GapsFilesService (GapsFilesRepository Repository)
    {
        this.Repository = Repository;
    }

    public GapsFiles byCode (String code)
    {
        return Repository.findByCodeAndDeletedFalse (code);
    }
}
