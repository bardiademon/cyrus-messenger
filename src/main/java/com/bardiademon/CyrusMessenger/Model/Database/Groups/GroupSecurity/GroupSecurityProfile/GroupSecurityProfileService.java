package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityProfile;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GroupSecurityProfileService
{
    public final GroupSecurityProfileRepository Repository;

    @Autowired
    public GroupSecurityProfileService (GroupSecurityProfileRepository Repository)
    {
        this.Repository = Repository;
    }


    public GroupSecurityProfile getSec (Groups group)
    {
        return Repository.findByGroupsId (group.getId ());
    }
}
