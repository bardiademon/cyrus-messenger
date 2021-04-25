package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public void suspend (GroupManagement groupManagement , MainAccount mainAccount , MainAccount suspendedBy , Class<?> requestFrom)
    {
        groupManagement.setSuspended (true);
        groupManagement.setSuspendedAt (LocalDateTime.now ());
        groupManagement.setSuspendedBy (suspendedBy);
        Repository.save (groupManagement);
        l.n (null  , mainAccount , null , Thread.currentThread ().getStackTrace () , null , requestFrom.toString ());
    }
}
