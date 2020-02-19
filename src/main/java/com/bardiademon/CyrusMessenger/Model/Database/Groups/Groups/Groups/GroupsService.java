package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups;

import com.bardiademon.CyrusMessenger.Model.Database.LinkForJoin.LinkForJoin;
import com.bardiademon.CyrusMessenger.Model.Database.LinkForJoin.LinkForJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GroupsService
{
    public final GroupsRepository Repository;
    private final LinkForJoinService linkForJoinService;

    @Autowired
    public GroupsService (GroupsRepository Repository , LinkForJoinService _LinkForJoinService)
    {
        this.Repository = Repository;
        this.linkForJoinService = _LinkForJoinService;
    }

    public boolean moreThanLimit (long idUser)
    {
        return (Repository.countByOwnerIdAndDeletedFalse (idUser) >= Groups.MAX_CREATE_GROUP);
    }

    public Groups hasUsername (String username)
    {
        return Repository.findByUsernameAndDeletedFalse (username);
    }

    public Groups hasLink (String link)
    {
        LinkForJoin linkForJoin = linkForJoinService.hasLinkGroup (link);
        if (linkForJoin == null) return null;

        return linkForJoin.getGroups ();
    }

    public Groups hasGroup (long idGroup)
    {
        return Repository.findById (idGroup);
    }
}
