package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityGap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GroupSecurityGapService
{
    public final GroupSecurityGapRepository Repository;

    @Autowired
    public GroupSecurityGapService (final GroupSecurityGapRepository Repository)
    {
        this.Repository = Repository;
    }
}
