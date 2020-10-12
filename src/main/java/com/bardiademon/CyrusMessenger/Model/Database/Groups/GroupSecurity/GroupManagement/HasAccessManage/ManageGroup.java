package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.IsManager;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.ILUGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.bardiademon.ID;


public final class ManageGroup
{
    private final Service service;
    private ID idGroup;
    private String groupname;
    private final MainAccount mainAccount;
    private final AccessLevel accessLevel;

    private AnswerToClient answerToClient;

    private IsManager manager;

    private boolean canManage = false;

    public ManageGroup (Service _Service , ID IdGroup , MainAccount _MainAccount , AccessLevel _AccessLevel)
    {
        this.service = _Service;
        this.idGroup = IdGroup;
        this.mainAccount = _MainAccount;
        this.accessLevel = _AccessLevel;
        can ();
    }

    public ManageGroup (Service _Service , String Groupname , MainAccount _MainAccount , AccessLevel _AccessLevel)
    {
        this.service = _Service;
        this.groupname = Groupname;
        this.mainAccount = _MainAccount;
        this.accessLevel = _AccessLevel;
        can ();
    }

    public ManageGroup (Service _Service , MainAccount _MainAccount , AccessLevel _AccessLevel)
    {
        this.service = _Service;
        this.mainAccount = _MainAccount;
        this.accessLevel = _AccessLevel;
        can ();
    }

    public void setGroupname (String groupname)
    {
        this.groupname = groupname;
    }

    public void setIdGroup (ID idGroup)
    {
        this.idGroup = idGroup;
    }

    private void can ()
    {
        if (checkId ())
        {
            if (mainAccount != null)
            {
                ILUGroup iluGroup = new ILUGroup (service.groupsService);
                if (idGroup != null) iluGroup.setId (idGroup.getId ());
                else iluGroup.setUsername (groupname);

                if (iluGroup.isValid ())
                {
                    manager = new IsManager (mainAccount , service.groupManagementService);
                    manager.setILUGroup (iluGroup);
                    if (manager.isManager ())
                    {
                        if (manager.isOwner () || manager.hasAccess (accessLevel)) canManage = true;
                        else setAnswerToClient (ValAnswer.you_do_not_have_access);
                    }
                    else setAnswerToClient (ValAnswer.you_are_not_a_manager);
                }
                else setAnswerToClient (ValAnswer.group_not_found);
            }
            else setAnswerToClient (ValAnswer.user_not_found);
        }
    }

    private void setAnswerToClient (ValAnswer answer)
    {
        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , answer.name ());
    }

    private boolean checkId ()
    {
        if (idGroup.isValid ()) return true;

        setAnswerToClient (ValAnswer.id_group_invalid);
        return false;
    }

    public final static class Service
    {
        public final MainAccountService mainAccountService;
        public final GroupsService groupsService;
        public final GroupManagementService groupManagementService;

        public Service (MainAccountService _MainAccountService , GroupsService _GroupsService , GroupManagementService _GroupManagementService)
        {
            this.mainAccountService = _MainAccountService;
            this.groupsService = _GroupsService;
            this.groupManagementService = _GroupManagementService;
        }

    }

    private enum ValAnswer
    {
        id_group_invalid, user_not_found, group_not_found, you_are_not_a_manager, you_do_not_have_access
    }

    public IsManager getManager ()
    {
        return manager;
    }

    public boolean canManage ()
    {
        return canManage;
    }

    public AnswerToClient getAnswerToClient ()
    {
        return answerToClient;
    }
}
