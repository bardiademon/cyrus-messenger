package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups;

import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.Usernames;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import org.springframework.lang.Nullable;

// ILUGroups => Id Link Username Groups
public class ILUGroup
{
    private long id;
    private String link;
    private String username;
    private final GroupsService groupsService;
    private UsernamesService usernamesService;

    @Nullable
    private Groups group;

    public ILUGroup ()
    {
        this (ThisApp.Context ().getBean (GroupsService.class) , ThisApp.Context ().getBean (UsernamesService.class));
    }

    public ILUGroup (GroupsService _GroupsService)
    {
        this (_GroupsService , ThisApp.Context ().getBean (UsernamesService.class));
    }

    public ILUGroup (GroupsService _GroupsService , UsernamesService _UsernamesService)
    {
        this.groupsService = _GroupsService;
        this.usernamesService = _UsernamesService;
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
                Usernames forGroup = usernamesService.findForGroup (username);
                if (forGroup != null) group = forGroup.getGroups ();
                else return false;
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

    @org.jetbrains.annotations.Nullable
    public Groups getGroup ()
    {
        return group;
    }
}
