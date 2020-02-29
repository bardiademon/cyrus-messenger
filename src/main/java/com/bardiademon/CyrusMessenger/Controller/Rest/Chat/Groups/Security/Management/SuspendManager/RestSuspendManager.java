package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.Security.Management.SuspendManager;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagement;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.IsManager;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.CanManageGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = Domain.RNChat.RNGroups.Security.RN_SECURITY_SUSPEND_MANAGER, method = RequestMethod.POST)
public final class RestSuspendManager
{

    private final UserLoginService userLoginService;

    private final CanManageGroup.Service service;

    @Autowired
    public RestSuspendManager (UserLoginService _UserLoginService ,
                               GroupsService _GroupsService ,
                               GroupManagementService _GroupManagementService , MainAccountService _MainAccountService)
    {
        this.userLoginService = _UserLoginService;
        service = new CanManageGroup.Service (_MainAccountService , _GroupsService , _GroupManagementService);
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient remove
            (HttpServletResponse res , HttpServletRequest req , @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestSuspendManager request)
    {
        AnswerToClient answerToClient;
        String router = Domain.RNChat.RNGroups.Security.RN_SECURITY_SUSPEND_MANAGER;
        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , SubmitRequestType.remove_admin);
        if (both.isOk ())
        {
            if (request != null)
            {
                if (request.getIdGroup ().isValid ())
                {
                    if (request.getIdUser ().isValid ())
                    {
                        assert both.getIsLogin () != null;
                        MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

                        CanManageGroup canManageGroup = new CanManageGroup (service , request.getIdGroup () , mainAccount , AccessLevel.del_admin);
                        if (canManageGroup.canManage ())
                        {
                            MainAccount mainAccountUser = service.mainAccountService.findId (request.getIdUser ().getId ());
                            if (mainAccountUser != null)
                            {
                                IsManager isManager = new IsManager (mainAccountUser , service.groupManagementService);
                                isManager.setILUGroup (canManageGroup.getManager ().getIluGroup ());
                                if (isManager.isManager ())
                                {
                                    if (!isManager.isOwner ())
                                    {
                                        GroupManagement groupManagement = isManager.getGroupManagement ();
                                        groupManagement.setSuspended (true);
                                        groupManagement.setSuspendedAt (LocalDateTime.now ());
                                        groupManagement.setSuspendedBy (mainAccount);
                                        service.groupManagementService.Repository.save (groupManagement);

                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.suspended.name ());
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.suspended.name ());
                                        r.n (mainAccount , SubmitRequestType.remove_admin , false);
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.cannot_suspend_owner_of_group.name ());
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.cannot_suspend_owner_of_group.name ()) , null);
                                        r.n (mainAccount , SubmitRequestType.remove_admin , true);
                                    }
                                }
                                else
                                {
                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.this_user_is_not_a_manager.name ());
                                    answerToClient.setReqRes (req , res);
                                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_user_is_not_a_manager.name ()) , null);
                                    r.n (mainAccount , SubmitRequestType.remove_admin , true);
                                }
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.id_user_not_found.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_user_not_found.name ()) , null);
                                r.n (mainAccount , SubmitRequestType.remove_admin , true);
                            }
                        }
                        else answerToClient = canManageGroup.getAnswerToClient ();
                    }
                    else answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_user_invalid.name ());
                }
                else answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_group_invalid.name ());
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , router , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("Request is null") , null);
                r.n (req.getRemoteAddr () , SubmitRequestType.remove_admin , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum ValAnswer
    {
        id_group_invalid, id_user_invalid, id_user_not_found, this_user_is_not_a_manager, cannot_suspend_owner_of_group, suspended
    }

}
