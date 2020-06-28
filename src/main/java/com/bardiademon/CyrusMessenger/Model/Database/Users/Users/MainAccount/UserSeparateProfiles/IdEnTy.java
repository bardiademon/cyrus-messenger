package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles;

import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

// IdEnTy , Id => id separate profile , EnTy => Enum Type
public final class IdEnTy
{

    private ID id;

    @JsonProperty("enum_type")
    private String enumType;

    public IdEnTy ()
    {
    }

    public long getId ()
    {
        return id.getId ();
    }

    public void setId (ID id)
    {
        this.id = id;
    }

    @JsonIgnore
    public ID getIdClass ()
    {
        return id;
    }

    public String getEnumType ()
    {
        return enumType;
    }

    public void setEnumType (String enumType)
    {
        this.enumType = enumType;
    }
}
