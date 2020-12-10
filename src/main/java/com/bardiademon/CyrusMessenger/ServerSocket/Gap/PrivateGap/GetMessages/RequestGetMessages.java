package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.GetMessages;

import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.PublicRequest;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestGetMessages extends PublicRequest
{
    private long id;

    private String type;

    @JsonProperty ("page")
    private int page;

    @JsonProperty ("personal_gaps_id")
    private long personalGapsId;

    public enum Type
    {
        user, group;

        public static Type to (String name)
        {
            try
            {
                return valueOf (name);
            }
            catch (Exception e)
            {
                l.n (Thread.currentThread ().getStackTrace () , e , ToJson.CreateClass.nj ("name" , name));
                return null;
            }
        }
    }

    public RequestGetMessages ()
    {
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public int getPage ()
    {
        return page;
    }

    public void setPage (int page)
    {
        this.page = page;
    }

    public long getPersonalGapsId ()
    {
        return personalGapsId;
    }

    public void setPersonalGapsId (long personalGapsId)
    {
        this.personalGapsId = personalGapsId;
    }
}
