package com.bardiademon.CyrusMessenger.Model.Database.Images;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class UploadedImagesService
{
    public final UploadedImagesRepository Repository;

    @Autowired
    public UploadedImagesService (UploadedImagesRepository Repository)
    {
        this.Repository = Repository;
    }
}
