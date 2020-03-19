package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Update.Security.Profile.ListShowFor.Add;

import com.bardiademon.CyrusMessenger.Controller.Rest.UsedRequests.R_IDUsername;

import java.util.List;

public final class RequestAddShowProfileFor
{
    List<R_IDUsername> list;

    private String add;

    public RequestAddShowProfileFor ()
    {
    }

    public List<R_IDUsername> getList ()
    {
        return list;
    }

    public void setList (List<R_IDUsername> list)
    {
        this.list = list;
    }

    public String getAdd ()
    {
        return add;
    }

    public void setAdd (String add)
    {
        this.add = add;
    }

    public enum Add
    {
        list_all_except, show_just;

        public static Add to (String add)
        {
            try
            {
                return valueOf (add);
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }
}
