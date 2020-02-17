package com.bardiademon.CyrusMessenger.Model.Database.LinkForJoin;

import com.bardiademon.CyrusMessenger.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class LinkForJoinService
{
    public final LinkForJoinRepository Repository;

    private final static Object Wait = new Object ();

    @Autowired
    public LinkForJoinService (LinkForJoinRepository Repository)
    {
        this.Repository = Repository;
    }

    private boolean createdLink;
    private String link;

    public LinkForJoin create (LinkForJoin.LinkFor linkFor)
    {
        createdLink = false;
        link = null;
        new Thread (() -> Code.CreateCodeIsNotExists (Code.CreateCodeLong (LinkForJoin.MAX_LEN_CODE) , 10 , (code , last) ->
        {
            if (hasLink (code) == null)
            {
                link = code;
                createdLink = true;
                synchronized (Wait)
                {
                    Wait.notify ();
                }
                return true;
            }
            else
            {
                if (last)
                {
                    synchronized (Wait)
                    {
                        Wait.notify ();
                    }
                }
                return last;
            }
        })).start ();

        synchronized (Wait)
        {
            try
            {
                Wait.wait ();
            }
            catch (InterruptedException ignored)
            {
            }
        }

        if (createdLink)
        {
            LinkForJoin linkForJoin = new LinkForJoin ();
            linkForJoin.setLink (link);
            linkForJoin.setLinkFor (linkFor);
            return Repository.save (linkForJoin);
        }
        else return null;
    }

    public LinkForJoin hasLink (String link)
    {
        return Repository.findByLinkAndDeletedFalse (link);
    }

    public LinkForJoin hasLinkGroup (String link)
    {
        return Repository.findByLinkAndDeletedFalseAndGroupsNotNull (link);
    }
}
