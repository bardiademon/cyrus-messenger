package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GapFilesService
{
    public final GapFilesRepository Repository;

    @Autowired
    public GapFilesService (GapFilesRepository Repository)
    {
        this.Repository = Repository;
    }

    public GapFiles byCode (String code)
    {
        return Repository.findByCodeAndDeletedFalse (code);
    }
}
