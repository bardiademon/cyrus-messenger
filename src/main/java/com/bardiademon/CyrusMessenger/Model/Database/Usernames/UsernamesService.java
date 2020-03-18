package com.bardiademon.CyrusMessenger.Model.Database.Usernames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class UsernamesService
{
    public final UsernamesRepository Repository;

    @Autowired
    public UsernamesService (UsernamesRepository Repository)
    {
        this.Repository = Repository;
    }

    public Usernames findForUser (String username)
    {
        return Repository.findByUsernameAndUsernameForAndDeletedFalseAndActiveTrueAndMainAccountActiveTrue (username , UsernameFor.user);
    }

    public Usernames findForGroup (String username)
    {
        return Repository.findByUsernameAndUsernameForAndDeletedFalseAndActiveTrue (username , UsernameFor.group);
    }
}
