package com.bardiademon.CyrusMessenger.Model.Database.DeletedOrEdited;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DeletedOrEditedService
{
    public final DeletedOrEditedRepository Repository;

    @Autowired
    public DeletedOrEditedService (DeletedOrEditedRepository Repository)
    {
        this.Repository = Repository;
    }
}
