package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.ThisManagerHaveAccess;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.ILUGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;

public final class IsManager
{
    private MainAccount mainAccount;
    private GroupManagementService groupManagementService;
    private Groups group;

    private ThisManagerHaveAccess thisManagerHaveAccess;

    private ILUGroup iluGroup;

    private GroupManagement groupManagement;

    private boolean isOwner;

    public IsManager (MainAccount _MainAccount , GroupManagementService _GroupManagementService)
    {
        this.mainAccount = _MainAccount;
        this.groupManagementService = _GroupManagementService;
    }

    public void setILUGroup (ILUGroup iluGroup)
    {
        this.iluGroup = iluGroup;
    }

    public boolean isManager ()
    {
        if (iluGroup == null) return false;
        else
        {
            group = iluGroup.getGroup ();
            if (group != null && group.getOwner ().getId () == mainAccount.getId ())
            {
                isOwner = true;
                return true;
            }
            else
            {
                groupManagement = groupManagementService.getGroupManagement (mainAccount.getId () , group.getId ());
                return (groupManagement != null);
            }
        }
    }

    public boolean hasAccess (AccessLevel _AccessLevel)
    {
        if (groupManagement == null) return false;
        else
        {
            thisManagerHaveAccess = new ThisManagerHaveAccess (groupManagement , _AccessLevel);
            return thisManagerHaveAccess.hasAccess ();
        }
    }

    public Groups getGroup ()
    {
        return group;
    }

    public GroupManagement getGroupManagement ()
    {
        return groupManagement;
    }

    public ThisManagerHaveAccess getThisManagerHaveAccess ()
    {
        return thisManagerHaveAccess;
    }

    public boolean isOwner ()
    {
        return isOwner;
    }

    public MainAccount getMainAccount ()
    {
        return mainAccount;
    }

    public ILUGroup getIluGroup ()
    {
        return iluGroup;
    }
}
