package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Block.NewBlock;

import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestNewBlock
{
    private String username;

    @JsonProperty ("id_user")
    private ID idUser;

    @JsonProperty ("plus_up_to")
    private String plusUpTo;

    private int extent;

    private boolean block;

    private String type;

    public RequestNewBlock ()
    {
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getPlusUpTo ()
    {
        return plusUpTo;
    }

    public void setPlusUpTo (String plusUpTo)
    {
        this.plusUpTo = plusUpTo;
    }

    public int getExtent ()
    {
        return extent;
    }

    public void setExtent (int extent)
    {
        this.extent = extent;
    }

    public boolean isBlock ()
    {
        return block;
    }

    public void setBlock (boolean block)
    {
        this.block = block;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public ID getIdUser ()
    {
        return idUser;
    }

    public void setIdUser (ID idUser)
    {
        this.idUser = idUser;
    }

    public enum PlusUpTo
    {
        minutes, hour, month, year
    }

    public PlusUpTo checkPlusToUp (String plusUpTo)
    {
        try
        {
            return PlusUpTo.valueOf (plusUpTo);
        }
        catch (Exception e)
        {
            return null;
        }
    }

}
