package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GapTypesService
{
    public final GapTypesRepository Repository;

    @Autowired
    public GapTypesService (GapTypesRepository Repository)
    {
        this.Repository = Repository;
    }
}
