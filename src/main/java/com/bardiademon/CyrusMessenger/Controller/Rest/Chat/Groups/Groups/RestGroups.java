package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.Groups;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.FindGroups.RestFindGroups;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityProfile.GroupSecurityProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroupService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping (value = Domain.RNChat.RNGroups.RN_GROUPS, method = RequestMethod.POST)
public final class RestGroups
{

    private UserLoginService userLoginService;
    private GroupsService groupsService;
    private GroupSecurityProfileService groupSecurityProfileService;
    private JoinGroupService joinGroupService;

    private RestGroups (UserLoginService _UserLoginService ,
                        GroupsService _GroupsService , GroupSecurityProfileService _GroupSecurityProfileService , JoinGroupService _JoinGroupService)
    {
        this.userLoginService = _UserLoginService;
        this.groupsService = _GroupsService;
        this.groupSecurityProfileService = _GroupSecurityProfileService;
        this.joinGroupService = _JoinGroupService;
    }

    @RequestMapping (value = {"" , "/" , "/{strOwnerUser}"})
    public AnswerToClient get
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @PathVariable (value = "strOwnerUser", required = false) String strOwnerUser)
    {
        AnswerToClient answerToClient;
        CheckBlockSystem checkBlockSystem = CBSIL.BSubmitRequest (req , SubmitRequestType.owner_groups);
        if (!checkBlockSystem.isBlocked ())
        {
            IsLogin isLogin = CBSIL.isLogin (codeLogin , userLoginService);
            if (isLogin.isValid ())
            {
                MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();
                checkBlockSystem = CBSIL.BSubmitRequest (mainAccount.getId () , SubmitRequestType.owner_groups);
                if (!checkBlockSystem.isBlocked ())
                {
                    if (!Str.IsEmpty (strOwnerUser))
                    {
                        OwnerUser ownerUser = OwnerUser.to (strOwnerUser);
                        if (ownerUser != null)
                        {
                            List<Groups> groups;
                            if (ownerUser.equals (OwnerUser.owner))
                                groups = groupsService.fromOwner (mainAccount.getId ());
                            else groups = joinGroupService.listGroupJoin (mainAccount.getId ());
                            if (groups != null && groups.size () > 0)
                            {
                                RestFindGroups restFindGroups = new RestFindGroups (groupsService , groupSecurityProfileService);
                                AnswerToClient byLink;
                                Map<String, Object> message;
                                Map<?, ?> infoGroup;
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , RestFindGroups.ValAnswer.found.name ());
                                List<Map<?, ?>> infoGroups = new ArrayList<> ();
                                for (Groups group : groups)
                                {
                                    try
                                    {
                                        byLink = restFindGroups.getInfoGroup (req , res , group , RestFindGroups.ValAnswer.link.name () , group.getLink ());
                                        message = byLink.getMessage ();
                                        if (message.get (AnswerToClient.CUK.answer.name ()).equals (RestFindGroups.ValAnswer.found.name ()))
                                        {
                                            infoGroup = (Map<?, ?>) message.get (RestFindGroups.ValAnswer.info_group.name ());
                                            if (ownerUser.equals (OwnerUser.owner) || !((group.getGroupSecurityProfile ()).isShowOwner ()))
                                                infoGroup.remove ("owner");

                                            infoGroups.add (infoGroup);
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        System.out.println (e.getMessage ());
                                        l.n (strOwnerUser , Domain.RNChat.RNGroups.RN_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , e , ToJson.To (group));
                                    }
                                }
                                answerToClient.put (KeyAnswer.info_groups.name () , infoGroups);
                                l.n (strOwnerUser , Domain.RNChat.RNGroups.RN_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , null , RestFindGroups.ValAnswer.found.name ());
                                r.n (req.getRemoteAddr () , SubmitRequestType.owner_groups , false);
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (strOwnerUser , Domain.RNChat.RNGroups.RN_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.not_found.name ());
                                r.n (req.getRemoteAddr () , SubmitRequestType.owner_groups , true);
                            }
                        }
                        else
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.owner_or_user_invalid.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (strOwnerUser , Domain.RNChat.RNGroups.RN_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.owner_or_user_invalid.name ());
                            r.n (req.getRemoteAddr () , SubmitRequestType.owner_groups , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.not_set_owner_or_user.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (strOwnerUser , Domain.RNChat.RNGroups.RN_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_set_owner_or_user.name ()) , null);
                        r.n (req.getRemoteAddr () , SubmitRequestType.owner_groups , true);
                    }
                }
                else
                {
                    answerToClient = checkBlockSystem.getAnswerToClient ();
                    answerToClient.setReqRes (req , res);
                    l.n (strOwnerUser , Domain.RNChat.RNGroups.RN_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("block by system") , null);
                }
            }
            else
            {
                answerToClient = isLogin.getAnswerToClient ();
                answerToClient.setReqRes (req , res);
                l.n (strOwnerUser , Domain.RNChat.RNGroups.RN_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("block by system") , null);
                r.n (req.getRemoteAddr () , SubmitRequestType.owner_groups , true);
            }
        }
        else
        {
            answerToClient = checkBlockSystem.getAnswerToClient ();
            answerToClient.setReqRes (req , res);
            l.n (strOwnerUser , Domain.RNChat.RNGroups.RN_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("block by system") , null);
        }
        return answerToClient;
    }

    private enum KeyAnswer
    {
        info_groups
    }

    private enum ValAnswer
    {
        not_set_owner_or_user, owner_or_user_invalid
    }

    private enum OwnerUser
    {
        owner, user;

        public static OwnerUser to (String ownerUser)
        {
            try
            {
                return valueOf (ownerUser);
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }

}
