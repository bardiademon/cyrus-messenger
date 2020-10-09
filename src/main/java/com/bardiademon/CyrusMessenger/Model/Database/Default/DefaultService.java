package com.bardiademon.CyrusMessenger.Model.Database.Default;

import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultService
{
    public final DefaultRepository Repository;

    @Autowired
    public DefaultService (DefaultRepository Repository)
    {
        this.Repository = Repository;
    }

    public Integer getInt (DefaultKey key)
    {
        Default aDefault = getDefault (key);
        try
        {
            if (aDefault != null)
            {
                if (aDefault.getTypeValue ().equals (DefaultType.integer))
                    return Integer.parseInt (aDefault.getValue ());
                else
                    throw new DefaultException (aDefault.toString ());
            }
            else
                throw new DefaultException (ToJson.CreateClass.n ("message" , "Default is null").put ("key" , key.name ()).toJson ());
        }
        catch (DefaultException ignored)
        {
        }
        return null;
    }

    public Long getLong (DefaultKey key)
    {
        Default aDefault = getDefault (key);
        try
        {
            if (aDefault != null)
            {
                if (aDefault.getTypeValue ().equals (DefaultType.lng))
                    return Long.parseLong (aDefault.getValue ());
                else
                    throw new DefaultException (aDefault.toString ());
            }
            else
                throw new DefaultException (ToJson.CreateClass.n ("message" , "Default is null").put ("key" , key.name ()).toJson ());
        }
        catch (DefaultException ignored)
        {
        }
        return null;
    }

    public Float getFloat (DefaultKey key)
    {
        Default aDefault = getDefault (key);
        try
        {
            if (aDefault != null)
            {
                if (aDefault.getTypeValue ().equals (DefaultType.flt))
                    return Float.parseFloat (aDefault.getValue ());
                else
                    throw new DefaultException (aDefault.toString ());
            }
            else
                throw new DefaultException (ToJson.CreateClass.n ("message" , "Default is null").put ("key" , key.name ()).toJson ());
        }
        catch (DefaultException ignored)
        {
        }
        return null;
    }

    public Double getDouble (DefaultKey key)
    {
        Default aDefault = getDefault (key);
        try
        {
            if (aDefault != null)
            {
                if (aDefault.getTypeValue ().equals (DefaultType.dbl))
                    return Double.parseDouble (aDefault.getValue ());
                else
                    throw new DefaultException (aDefault.toString ());
            }
            else
                throw new DefaultException (ToJson.CreateClass.n ("message" , "Default is null").put ("key" , key.name ()).toJson ());
        }
        catch (DefaultException ignored)
        {
        }
        return null;
    }

    public String getString (DefaultKey key)
    {
        Default aDefault = getDefault (key);
        try
        {
            if (aDefault != null)
            {
                if (aDefault.getTypeValue ().equals (DefaultType.string))
                    return aDefault.getValue ();
                else
                    throw new DefaultException (aDefault.toString ());
            }
            else
                throw new DefaultException (ToJson.CreateClass.n ("message" , "Default is null").put ("key" , key.name ()).toJson ());
        }
        catch (DefaultException ignored)
        {
        }
        return null;
    }

    public Default getDefault (DefaultKey key)
    {
        return Repository.findByKey (key);
    }

}
