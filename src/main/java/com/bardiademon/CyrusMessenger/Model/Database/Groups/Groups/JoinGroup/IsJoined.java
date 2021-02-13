package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup;


import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.ID;

public final class IsJoined
{
    private JoinGroup joined;
    private final MainAccount mainAccount;
    private final ID idGroup;
    private final JoinGroupService joinGroupService;

    public IsJoined (final MainAccount _MainAccount , final ID IdGroup)
    {
        this (((JoinGroupService) This.Services ().Get (JoinGroupService.class)) , _MainAccount , IdGroup);
    }

    public IsJoined (final JoinGroupService _JoinGroupService , final MainAccount _MainAccount , final ID IdGroup)
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
