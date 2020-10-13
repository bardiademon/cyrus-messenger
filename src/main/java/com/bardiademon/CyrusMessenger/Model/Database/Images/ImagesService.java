package com.bardiademon.CyrusMessenger.Model.Database.Images;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ImagesService
{
    public final ImagesRepository Repository;

    @Autowired
    public ImagesService (ImagesRepository Repository)
    {
        this.Repository = Repository;
    }

}
