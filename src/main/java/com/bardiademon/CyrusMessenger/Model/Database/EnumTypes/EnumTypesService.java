package com.bardiademon.CyrusMessenger.Model.Database.EnumTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class EnumTypesService
{
    public final EnumTypesRepository Repository;

    @Autowired
    public EnumTypesService (EnumTypesRepository Repository)
    {
        this.Repository = Repository;
    }
}
