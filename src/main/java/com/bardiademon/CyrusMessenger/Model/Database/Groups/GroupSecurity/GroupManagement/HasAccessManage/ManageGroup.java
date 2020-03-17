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
    private final ID idGroup;
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

    private void can ()
    {
        if (checkId ())
        {
            if (mainAccount != null)
            {
                ILUGroup iluGroup = new ILUGroup (service.groupsService);
                iluGroup.setId (idGroup.getId ());
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
