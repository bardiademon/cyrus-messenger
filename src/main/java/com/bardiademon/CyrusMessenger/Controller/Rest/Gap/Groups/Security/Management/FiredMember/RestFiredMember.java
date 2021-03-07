package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Groups.Security.Management.FiredMember;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.FiredFromGroup.FiredFromGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.FiredFromGroup.FiredFromGroupService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.IsManager;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.ManageGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.IsJoined;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroupService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = Domain.RNGap.RNGroups.Security.RN_SECURITY_FIRED_MEMBER, method = RequestMethod.POST)
public final class RestFiredMember
{

    private final UserLoginService userLoginService;
    private final JoinGroupService joinGroupService;
    private final FiredFromGroupService firedFromGroupService;

    private final ManageGroup.Service service;

    @Autowired
    public RestFiredMember
            (UserLoginService _UserLoginService ,
             GroupsService _GroupsService ,
             GroupManagementService _GroupManagementService ,
             MainAccountService _MainAccountService , JoinGroupService _JoinGroupService , FiredFromGroupService _FiredFromGroupService)
    {
        this.userLoginService = _UserLoginService;
        this.joinGroupService = _JoinGroupService;
        this.firedFromGroupService = _FiredFromGroupService;
        service = new ManageGroup.Service (_MainAccountService , _GroupsService , _GroupManagementService);
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient remove
            (HttpServletResponse res , HttpServletRequest req , @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestFiredMember request)
    {
        AnswerToClient answerToClient = null;

        String router = Domain.RNGap.RNGroups.Security.RN_SECURITY_FIRED_MEMBER;
        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , SubmitRequestType.remove_group_member);
        if (both.isOk ())
        {
            if (request != null)
            {
                assert both.getIsLogin () != null;
                MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
                if (!Str.IsEmpty (request.getWhy ()))
                {
                    if (request.getIdUser ().isValid ())
                    {
                        MainAccount mainAccountUser = service.mainAccountService.findId (request.getIdUser ().getId ());
                        if (mainAccountUser != null)
                        {
                            ManageGroup manageGroup = new ManageGroup (service , request.getIdGroup () , mainAccount , AccessLevel.dismiss_user);
                            if (manageGroup.canManage ())
                            {
                                IsJoined isJoined = null;

                                boolean dismiss = false;
                                IsManager isManager = new IsManager (mainAccountUser , service.groupManagementService);
                                isManager.setILUGroup (manageGroup.getManager ().getIluGroup ());
                                if (isManager.isManager ())
                                {
                                    if (!isManager.isOwner ())
                                    {
                                        ManageGroup canManageAccessLevelGroup = new ManageGroup (service , request.getIdGroup () , mainAccount , AccessLevel.del_admin);
                                        if (manageGroup.canManage ())
                                        {
                                            service.groupManagementService
                                                    .suspend (isManager.getGroupManagement () , mainAccount , mainAccount , this.getClass ());

                                            dismiss = true;
                                        }
                                        else answerToClient = canManageAccessLevelGroup.getAnswerToClient ();
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.IdInvalid (ValAnswer.this_user_owns_the_group);
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.this_user_owns_the_group));
                                        r.n (mainAccount , SubmitRequestType.remove_group_member , true);
                                    }
                                }
                                else
                                {
                                    isJoined = new IsJoined (joinGroupService , mainAccountUser , request.getIdGroup ());
                                    if (isJoined.is ()) dismiss = true;
                                    else
                                    {
                                        answerToClient = AnswerToClient.IdInvalid (ValAnswer.this_user_is_not_a_member_of_group);
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.this_user_is_not_a_member_of_group));
                                        r.n (mainAccount , SubmitRequestType.remove_group_member , true);
                                    }
                                }

                                if (dismiss)
                                {
                                    assert isJoined != null;
                                    JoinGroup joined = isJoined.getJoined ();
                                    joined.setTimeLeave (LocalDateTime.now ());
                                    joined.setLeaveGroup (true);
                                    joinGroupService.Repository.save (joined);

                                    FiredFromGroup firedFromGroup = new FiredFromGroup ();
                                    firedFromGroup.setFiredAt (LocalDateTime.now ());
                                    firedFromGroup.setFiredBy (mainAccount);
                                    firedFromGroup.setGroup (manageGroup.getManager ().getGroup ());
                                    firedFromGroup.setMainAccount (mainAccountUser);
                                    firedFromGroup.setWhy (request.getWhy ());

                                    if (request.getValidityTime () < 1)
                                        request.setValidityTime (FiredFromGroup.DEFAULT_VALIDITY_TIME);
                                    firedFromGroup.setValidityTime (LocalDateTime.now ().plusMinutes (request.getValidityTime ()));

                                    firedFromGroupService.Repository.save (firedFromGroup);

                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.fired);
                                    answerToClient.setReqRes (req , res);
                                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.this_user_is_not_a_member_of_group));
                                    r.n (mainAccount , SubmitRequestType.remove_group_member , false);
                                }

                            }
                            else answerToClient = manageGroup.getAnswerToClient ();
                        }
                        else
                        {
                            answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_user_not_found);
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.id_user_invalid));
                            r.n (mainAccount , SubmitRequestType.remove_group_member , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_user_invalid);
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.id_user_invalid));
                        r.n (mainAccount , SubmitRequestType.remove_group_member , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid (ValAnswer.please_specify_the_cause);
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.please_specify_the_cause));
                    r.n (mainAccount , SubmitRequestType.remove_group_member , true);
                }
            }
            else answerToClient = AnswerToClient.RequestIsNull ();
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum ValAnswer
    {
        id_user_invalid, id_user_not_found,
        this_user_owns_the_group, this_user_is_not_a_member_of_group, please_specify_the_cause, fired
    }

}
