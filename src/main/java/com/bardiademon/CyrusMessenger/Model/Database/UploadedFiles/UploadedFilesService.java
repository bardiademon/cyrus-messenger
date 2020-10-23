package com.bardiademon.CyrusMessenger.Model.Database.UploadedFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class UploadedFilesService
{
    public final UploadedFilesRepository Repository;

    @Autowired
    public UploadedFilesService (UploadedFilesRepository Repository)
    {
        this.Repository = Repository;
    }

}
