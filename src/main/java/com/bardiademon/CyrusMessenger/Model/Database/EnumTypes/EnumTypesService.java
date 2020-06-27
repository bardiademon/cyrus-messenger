package com.bardiademon.CyrusMessenger.Model.Database.EnumTypes;

import com.bardiademon.CyrusMessenger.bardiademon.ID;
import java.util.ArrayList;
import java.util.List;
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

    public List <ETIdName> getEnumType (long idSep)
    {
        List <Object[]> enumType = Repository.getEnumType (idSep);
        if (enumType != null && enumType.size () > 0)
        {
            List <ETIdName> etIdNames = new ArrayList <> ();
            for (Object[] objEnum : enumType)
            {
                ETIdName etIdName = new ETIdName ();
                etIdName.setId (new ID (objEnum[0]));
                etIdName.setName (objEnum[1].toString ());
                etIdNames.add (etIdName);
            }
            System.gc ();
            return etIdNames;
        }
        else return null;
    }
}
