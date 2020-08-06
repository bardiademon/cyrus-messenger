package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.JoinGroup;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedFor;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.FiredFromGroup.FiredFromGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.FiredFromGroup.FiredFromGroupService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagement;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityProfile.GroupSecurityProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityProfile.GroupSecurityProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroupService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = Domain.RNChat.RNGroups.RN_JOIN_GROUP, method = RequestMethod.POST)
public final class RestJoinGroup
{

    private final GroupsService groupsService;
    private final GroupSecurityProfileService groupSecurityProfileService;
    private final JoinGroupService joinGroupService;
    private final GroupManagementService groupManagementService;
    private final FiredFromGroupService firedFromGroupService;

    public RestJoinGroup (GroupsService _GroupsService ,
                          GroupSecurityProfileService _GroupSecurityProfileService , JoinGroupService _JoinGroupService ,
                          GroupManagementService _GroupManagementService , FiredFromGroupService _FiredFromGroupService)
    {
        this.groupsService = _GroupsService;
        this.groupSecurityProfileService = _GroupSecurityProfileService;
        this.joinGroupService = _JoinGroupService;
        this.groupManagementService = _GroupManagementService;
        this.firedFromGroupService = _FiredFromGroupService;
    }

    @RequestMapping (value = {"" , "/" , "/{id}" , "/{id}/leave"})
    public AnswerToClient join (HttpServletResponse res , HttpServletRequest req , @PathVariable (value = "id", required = false) String idStr ,
                                @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        String path = req.getServletPath ();
        AnswerToClient answerToClient;
        String reqJson = ToJson.CreateClass.n ("id" , idStr).toJson ();
        CheckBlockSystem checkBlockSystem = new CheckBlockSystem (req , BlockedFor.submit_request , SubmitRequestType.join_group.name ());
        if (!checkBlockSystem.isBlocked ())
        {
            IsLogin isLogin = new IsLogin (codeLogin);
            if (isLogin.isValid ())
            {
                MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();
                checkBlockSystem = new CheckBlockSystem (mainAccount.getId () , BlockedFor.submit_request , SubmitRequestType.join_group.name ());
                if (!checkBlockSystem.isBlocked ())
                {
                    try
                    {
                        long id;
                        if (idStr != null && !Str.IsEmpty (idStr))
                        {
                            if (idStr.matches ("[0-9]*") && (id = Long.parseLong (idStr)) > 0)
                            {
                                Groups group = groupsService.hasGroup (id);
                                if (group != null)
                                {
                                    if (group.getOwner ().getId () != mainAccount.getId ())
                                    {
                                        JoinGroup joined = joinGroupService.isJoined (group.getId () , mainAccount.getId ());
                                        if (joined == null)
                                        {
                                            if (!path.contains ("leave"))
                                            {
                                                GroupSecurityProfile securityProfile = groupSecurityProfileService.getSec (group);
                                                if (securityProfile.isShowInSearch () && !securityProfile.isFamilyGroup () && securityProfile.isCanJoinGroup ())
                                                {
                                                    FiredFromGroup fired = firedFromGroupService.isFired (group.getId () , mainAccount.getId ());
                                                    if (fired == null)
                                                    {
                                                        Integer maxMember = group.getGroupSecurityProfile ().getMaxMember ();
                                                        if (group.getMembers ().size () >= maxMember)
                                                        {
                                                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.group_members_have_been_completed.name ());
                                                            answerToClient.setReqRes (req , res);
                                                            l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.group_members_have_been_completed.name ()) , null);
                                                            r.n (mainAccount , SubmitRequestType.join_group , true);
                                                        }
                                                        else
                                                        {
                                                            if (group.getGroupSecurityProfile ().isCanJoinGroup ())
                                                            {
                                                                JoinGroup joinGroup = new JoinGroup ();
                                                                joinGroup.setGroups (group);
                                                                joinGroup.setMainAccount (mainAccount);
                                                                joinGroup.setJoinBy (JoinGroup.JoinBy.the_user_himself);
                                                                joinGroupService.Repository.save (joinGroup);

                                                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.joined.name ());
                                                                answerToClient.put (AnswerToClient.CUK.time.name () , Time.toString (joinGroup.getTimeJoin ()));
                                                                answerToClient.setReqRes (req , res);
                                                                l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.joined.name ());
                                                                r.n (mainAccount , SubmitRequestType.join_group , false);
                                                            }
                                                            else
                                                            {
                                                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.joining_is_disabled.name ());
                                                                l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.joining_is_disabled.name ()) , null);
                                                                r.n (mainAccount , SubmitRequestType.join_group , true);
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        answerToClient = firedFromGroupService.createAnswerToClient (fired);
                                                        l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.group_members_have_been_completed.name ()) , null);
                                                        r.n (mainAccount , SubmitRequestType.join_group , true);
                                                    }
                                                }
                                                else
                                                {
                                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.joining_has_been_disabled.name ());
                                                    answerToClient.setReqRes (req , res);
                                                    l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.joining_has_been_disabled.name ()) , null);
                                                    r.n (mainAccount , SubmitRequestType.join_group , true);
                                                }
                                            }
                                            else
                                            {
                                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.you_not_already_joined.name ());
                                                answerToClient.setReqRes (req , res);
                                                l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_not_already_joined.name ()) , null);
                                                r.n (mainAccount , SubmitRequestType.join_group , true);
                                            }
                                        }
                                        else
                                        {
                                            if (!path.contains ("leave"))
                                            {
                                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.you_already_joined.name ());
                                                answerToClient.put (AnswerToClient.CUK.time.name () , Time.toString (joined.getTimeJoin ()));
                                                answerToClient.setReqRes (req , res);
                                                l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_already_joined.name ()) , null);
                                                r.n (mainAccount , SubmitRequestType.join_group , true);
                                            }
                                            else
                                            {
                                                if (joined.isLeaveGroup ())
                                                {
                                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.you_not_already_joined.name ());
                                                    answerToClient.setReqRes (req , res);
                                                    l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_not_already_joined.name ()) , null);
                                                    r.n (mainAccount , SubmitRequestType.join_group , true);
                                                }
                                                else
                                                {
                                                    joined.setLeaveGroup (true);
                                                    joined.setTimeLeave (LocalDateTime.now ());
                                                    joinGroupService.Repository.save (joined);

                                                    GroupManagement groupManagement = groupManagementService.getGroupManagement (mainAccount.getId () , group.getId ());

                                                    if (groupManagement != null)
                                                    {
                                                        groupManagement.setSuspended (true);
                                                        groupManagement.setSuspendedAt (LocalDateTime.now ());
                                                        groupManagement.setSuspendedBy (mainAccount);
                                                        groupManagementService.Repository.save (groupManagement);
                                                    }

                                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.leaved.name ());
                                                    answerToClient.setReqRes (req , res);
                                                    l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.leaved.name ());
                                                    r.n (mainAccount , SubmitRequestType.join_group , false);
                                                }
                                            }

                                        }
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.you_own_the_group.name ());
                                        answerToClient.setReqRes (req , res);
                                        l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_own_the_group.name ()) , null);
                                        r.n (mainAccount , SubmitRequestType.join_group , true);
                                    }
                                }
                                else
                                {
                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.group_not_found.name ());
                                    answerToClient.setReqRes (req , res);
                                    l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.group_not_found.name ()) , null);
                                    r.n (req.getRemoteAddr () , SubmitRequestType.join_group , true);
                                }
                            }
                            else throw new Exception (AnswerToClient.CUV.id_invalid.name ());
                        }
                        else
                        {
                            answerToClient = AnswerToClient.RequestIsNull ();
                            answerToClient.setReqRes (req , res);
                            l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("request is null") , null);
                            r.n (req.getRemoteAddr () , SubmitRequestType.join_group , true);
                        }
                    }
                    catch (Exception e)
                    {
                        if (e.getMessage () != null && e.getMessage ().matches (AnswerToClient.CUV.id_invalid.name ()))
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.id_invalid.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , e , AnswerToClient.CUV.id_invalid.name ());
                            r.n (req.getRemoteAddr () , SubmitRequestType.join_group , false);
                        }
                        else
                        {
                            e.printStackTrace ();
                            answerToClient = AnswerToClient.ServerError ();
                            answerToClient.setReqRes (req , res);
                            l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , e , "server error");
                        }
                    }
                }
                else
                {
                    answerToClient = checkBlockSystem.getAnswerToClient ();
                    answerToClient.setReqRes (req , res);
                    l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("block by system") , null);
                }
            }
            else
            {
                answerToClient = isLogin.getAnswerToClient ();
                answerToClient.setReqRes (req , res);
                l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("not login") , null);
                r.n (req.getRemoteAddr () , SubmitRequestType.join_group , true);
            }
        }
        else
        {
            answerToClient = checkBlockSystem.getAnswerToClient ();
            answerToClient.setReqRes (req , res);
            l.n (reqJson , Domain.RNChat.RNGroups.RN_JOIN_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("block by system") , null);
        }
        return answerToClient;
    }

    public enum ValAnswer
    {
        group_not_found, you_own_the_group, joining_has_been_disabled, joined, you_already_joined,
        you_not_already_joined, leaved, group_members_have_been_completed, joining_is_disabled
    }

}
