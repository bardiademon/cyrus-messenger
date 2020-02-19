package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class JoinGroupService
{
    public final JoinGroupRepository Repository;

    @Autowired
    public JoinGroupService (JoinGroupRepository Repository)
    {
        this.Repository = Repository;
    }

    public JoinGroup isJoined (long idUser)
    {
        return Repository.findByMainAccountIdAndLeaveGroupFalse (idUser);
    }
}
