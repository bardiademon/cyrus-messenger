package com.bardiademon.CyrusMessenger.Model.Database.EnumTypes;


import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.fasterxml.jackson.annotation.JsonIgnore;

// ETIdName => ET => EnumTypes , id , enumType
public final class ETIdName
{
    private ID id;
    private String name;

    public ETIdName ()
    {
    }

    @JsonIgnore
    public ID getIdClass ()
    {
        return id;
    }

    public long getId ()
    {
        return id.getId ();
    }

    public void setId (ID id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }
}
