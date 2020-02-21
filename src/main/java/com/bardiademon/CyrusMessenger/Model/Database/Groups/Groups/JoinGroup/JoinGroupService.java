package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public final class JoinGroupService
{
    public final JoinGroupRepository Repository;

    @Autowired
    public JoinGroupService (JoinGroupRepository Repository)
    {
        this.Repository = Repository;
    }

    public JoinGroup isJoined (long idGroup , long idUser)
    {
        return Repository.findByMainAccountIdAndGroupsIdAndLeaveGroupFalse (idGroup , idUser);
    }


    public List<Groups> listGroupJoin (long idUser)
    {
        List<JoinGroup> joinGroups = Repository.findByMainAccountIdAndLeaveGroupFalse (idUser);
        if (joinGroups != null && joinGroups.size () > 0)
        {
            List<Groups> groups = new ArrayList<> ();
            for (JoinGroup joinGroup : joinGroups) groups.add (joinGroup.getGroups ());
            return groups;
        }
        else return null;
    }
}
