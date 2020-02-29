package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup;


import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.ID;

public final class IsJoined
{
    private JoinGroup joined;
    private MainAccount mainAccount;
    private ID idGroup;
    private JoinGroupService joinGroupService;

    public IsJoined (JoinGroupService _JoinGroupService , MainAccount _MainAccount , ID IdGroup)
    {
        this.joinGroupService = _JoinGroupService;
        this.mainAccount = _MainAccount;
        this.idGroup = IdGroup;
    }

    public boolean is ()
    {
        if (mainAccount == null || idGroup == null || !idGroup.isValid ()) return false;
        else
        {
            joined = joinGroupService.isJoined (idGroup.getId () , mainAccount.getId ());
            return (joined != null);
        }
    }

    public JoinGroup getJoined ()
    {
        return joined;
    }
}
