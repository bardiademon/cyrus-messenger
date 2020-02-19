package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagementAccessLevel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GroupManagementAccessLevelService
{
    public final GroupManagementAccessLevelRepository Repository;

    @Autowired
    public GroupManagementAccessLevelService (GroupManagementAccessLevelRepository Repository)
    {
        this.Repository = Repository;
    }
}
