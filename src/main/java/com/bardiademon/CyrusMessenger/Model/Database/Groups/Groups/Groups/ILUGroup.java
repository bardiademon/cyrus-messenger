package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups;

import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.CyrusMessengerApplication;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import org.springframework.lang.Nullable;

// ILUGroups => Id Link Username Groups
public class ILUGroup
{
    private long id;
    private String link;
    private String username;
    private final GroupsService groupsService;

    @Nullable
    private Groups group;

    public ILUGroup ()
    {
        this (CyrusMessengerApplication.Context ().getBean (GroupsService.class));
    }

    public ILUGroup (GroupsService _GroupsService)
    {
        this.groupsService = _GroupsService;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public void setLink (String link)
    {
        this.link = link;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public boolean isValid ()
    {
        if (id > 0)
        {
            group = groupsService.Repository.findById (id);
            if (group != null) return true;
        }

        if (group == null && !Str.IsEmpty (username))
        {
            if (new VUsername (username).check ())
            {
                group = groupsService.hasUsername (username);
                if (group != null) return true;
            }
        }

        if (group == null && !Str.IsEmpty (link))
        {
            if (link.matches ("[0-9A-Za-z]*"))
            {
                group = groupsService.hasLink (link);
                return group != null;
            }
        }
        return false;
    }

    public Groups getGroup ()
    {
        return group;
    }
}
