package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GroupManagementService
{
    public GroupManagementRepository Repository;

    @Autowired
    public GroupManagementService (GroupManagementRepository Repository)
    {
        this.Repository = Repository;
    }

    public GroupManagement getGroupManagement (long idUser , long idGroup)
    {
        return Repository.findByMainAccountIdAndGroupsIdAndSuspendedFalse (idUser , idGroup);
    }
}
