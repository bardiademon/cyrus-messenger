package com.bardiademon.CyrusMessenger.Model.Database.Default;

import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
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
                    throw new Exception ("Error type " + ToJson.CreateClass.nj ("type" , aDefault.getTypeValue ().name ()));
            }
            else
                throw new Exception ("Default is null");
        }
        catch (Exception e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , ToJson.CreateClass.nj ("key" , key.name ()));
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
                    throw new Exception ("Error type " + ToJson.CreateClass.nj ("type" , aDefault.getTypeValue ().name ()));
            }
            else
                throw new Exception ("Default is null");
        }
        catch (Exception e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , ToJson.CreateClass.nj ("key" , key.name ()));
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
                    throw new Exception ("Error type " + ToJson.CreateClass.nj ("type" , aDefault.getTypeValue ().name ()));
            }
            else
                throw new Exception ("Default is null");
        }
        catch (Exception e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , ToJson.CreateClass.nj ("key" , key.name ()));
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
                    throw new Exception ("Error type " + ToJson.CreateClass.nj ("type" , aDefault.getTypeValue ().name ()));
            }
            else
                throw new Exception ("Default is null");
        }
        catch (Exception e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , ToJson.CreateClass.nj ("key" , key.name ()));
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
                    throw new Exception ("Error type " + ToJson.CreateClass.nj ("type" , aDefault.getTypeValue ().name ()));
            }
            else
                throw new Exception ("Default is null");
        }
        catch (Exception e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , ToJson.CreateClass.nj ("key" , key.name ()));
        }
        return null;
    }

    public Default getDefault (DefaultKey key)
    {
        return Repository.findByKey (key);
    }

}
