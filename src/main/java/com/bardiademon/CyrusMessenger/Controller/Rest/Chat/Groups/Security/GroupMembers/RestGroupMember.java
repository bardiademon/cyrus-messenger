package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.Security.GroupMembers;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel.CheckUserAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagement;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagementAccessLevel.GroupManagementAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityProfile.GroupSecurityProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroupService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping (value = Domain.RNChat.RNGroups.Security.RN_SECURITY_GROUP_MEMBERS, method = RequestMethod.POST)
public final class RestGroupMember
{

    private final UserLoginService userLoginService;
    private final GroupsService groupsService;
    private final JoinGroupService joinGroupService;
    private final MainAccountService mainAccountService;
    private final GroupManagementService groupManagementService;

    private final CheckUserAccessLevel.ServiceProfile serviceProfile;

    @Autowired
    public RestGroupMember
            (UserLoginService _UserLoginService ,
             GroupsService _GroupsService ,
             JoinGroupService _JoinGroupService ,
             MainAccountService _MainAccountService ,
             ShowProfileForService _ShowProfileForService , UserContactsService _UserContactsService ,
             UserFriendsService _UserFriendsService , SecurityUserProfileService _SecurityUserProfileService ,
             UserBlockedService _UserBlockedService , GroupManagementService _GroupManagementService)
    {
        this.userLoginService = _UserLoginService;
        this.groupsService = _GroupsService;
        this.joinGroupService = _JoinGroupService;
        this.mainAccountService = _MainAccountService;
        this.groupManagementService = _GroupManagementService;
        this.serviceProfile = new CheckUserAccessLevel.ServiceProfile (_ShowProfileForService , _UserContactsService , _UserFriendsService , _SecurityUserProfileService , _UserBlockedService);
    }

    @RequestMapping (value = {"" , "/" , "/{id_group}"})
    public AnswerToClient get
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @PathVariable (value = "id_group", required = false) String strIdGroups)
    {
        AnswerToClient answerToClient = null;
        CheckBlockSystem blockSystem = CBSIL.BSubmitRequest (req , SubmitRequestType.group_members);
        if (!blockSystem.isBlocked ())
        {
            IsLogin isLogin = CBSIL.isLogin (codeLogin , userLoginService);
            if (isLogin.isValid ())
            {
                MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();
                blockSystem = CBSIL.BSubmitRequest (mainAccount.getId () , SubmitRequestType.group_members);
                if (!blockSystem.isBlocked ())
                {
                    boolean errorIdGroup;
                    long idGroup = 0;
                    try
                    {
                        if (Str.IsEmpty (strIdGroups)) throw new Exception ("is empty id group");
                        else
                        {
                            if (!strIdGroups.matches ("[0-9]*")) throw new Exception ("id invalid => not a number");
                            else
                            {
                                idGroup = Long.parseLong (strIdGroups);
                                if (idGroup > 0) errorIdGroup = false;
                                else throw new Exception ("id <= 0");
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.id_invalid.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (strIdGroups , Domain.RNChat.RNGroups.Security.RN_SECURITY_GROUP_MEMBERS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.id_invalid.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                        r.n (mainAccount , SubmitRequestType.group_members , true);
                        errorIdGroup = true;
                    }

                    if (!errorIdGroup)
                    {
                        Groups group = groupsService.hasGroup (idGroup);
                        if (group == null)
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.group_not_found.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (strIdGroups , Domain.RNChat.RNGroups.Security.RN_SECURITY_GROUP_MEMBERS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.group_not_found.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                            r.n (mainAccount , SubmitRequestType.group_members , true);
                        }
                        else
                        {
                            boolean isOwner = (group.getOwner ().getId () == mainAccount.getId ());

                            boolean isManagement = false;
                            boolean managementHasAccess = false;
                            boolean managementHasAccessHiddenMember = false;
                            if (!isOwner)
                            {
                                GroupManagement groupManagement
                                        = groupManagementService.getGroupManagement (mainAccount.getId () , group.getId ());

                                if (groupManagement != null)
                                {
                                    isManagement = true;
                                    GroupManagementAccessLevel accessLevel = groupManagement.getAccessLevel ();
                                    managementHasAccess = (accessLevel.isShowListMember ());
                                    managementHasAccessHiddenMember = (accessLevel.isShowMemberHidden ());
                                }
                            }

                            JoinGroup joined = null;
                            if (!isOwner && !isManagement)
                                joined = joinGroupService.isJoined (group.getId () , mainAccount.getId ());

                            if (isOwner || isManagement || joined != null)
                            {
                                GroupSecurityProfile groupSecurityProfile = group.getGroupSecurityProfile ();
                                if (isOwner || (isManagement && managementHasAccess) || (!isManagement && groupSecurityProfile.isShowListMember () && groupSecurityProfile.isShowNumberOfMember ()))
                                {
                                    List<JoinGroup> members = group.getMembers ();
                                    if (members != null && members.size () > 0)
                                    {
                                        CheckUserAccessLevel accessLevel;
                                        MainAccount memberMainAccount;
                                        List<Long> idMembers = new ArrayList<> ();

                                        if (groupSecurityProfile.isShowOwner ())
                                            idMembers.add (group.getOwner ().getId ());

                                        for (JoinGroup member : members)
                                        {
                                            memberMainAccount = member.getMainAccount ();
                                            accessLevel = new CheckUserAccessLevel (mainAccount , memberMainAccount , mainAccountService);
                                            accessLevel.setServiceProfile (serviceProfile);
                                            if (isOwner || (mainAccount.getId () == memberMainAccount.getId ()) || (isManagement && managementHasAccessHiddenMember) || (!isManagement && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_in_group) && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_id)))
                                                idMembers.add (memberMainAccount.getId ());
                                        }

                                        if (idMembers.size () > 0)
                                        {
                                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());
                                            answerToClient.put (ValAnswer.id_members.name () , idMembers);
                                        }
                                        else
                                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found.name ());

                                        answerToClient.setReqRes (req , res);
                                        l.n (strIdGroups , Domain.RNChat.RNGroups.Security.RN_SECURITY_GROUP_MEMBERS , null , answerToClient , Thread.currentThread ().getStackTrace () , null , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                        r.n (mainAccount , SubmitRequestType.group_members , false);
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.no_members_found.name ());
                                        answerToClient.setReqRes (req , res);
                                        l.n (strIdGroups , Domain.RNChat.RNGroups.Security.RN_SECURITY_GROUP_MEMBERS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.no_members_found.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                        r.n (mainAccount , SubmitRequestType.group_members , true);
                                    }
                                }
                                else
                                {
                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , AnswerToClient.CUV.access_has_been_disabled.name ());
                                    answerToClient.setReqRes (req , res);
                                    l.n (strIdGroups , Domain.RNChat.RNGroups.Security.RN_SECURITY_GROUP_MEMBERS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.access_has_been_disabled.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                    r.n (mainAccount , SubmitRequestType.group_members , true);
                                }
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.you_are_not_a_member_of_the_group.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (strIdGroups , Domain.RNChat.RNGroups.Security.RN_SECURITY_GROUP_MEMBERS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_are_not_a_member_of_the_group.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                r.n (mainAccount , SubmitRequestType.group_members , true);
                            }

                        }
                    }
                }
                else
                {
                    answerToClient = blockSystem.getAnswerToClient ();
                    answerToClient.setReqRes (req , res);
                    l.n (strIdGroups , Domain.RNChat.RNGroups.Security.RN_SECURITY_GROUP_MEMBERS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("blocked by system") , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                }
            }
            else
            {
                answerToClient = isLogin.getAnswerToClient ();
                answerToClient.setReqRes (req , res);
                l.n (strIdGroups , Domain.RNChat.RNGroups.Security.RN_SECURITY_GROUP_MEMBERS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("not login") , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                r.n (req.getRemoteAddr () , SubmitRequestType.group_members , true);
            }
        }
        else
        {
            answerToClient = blockSystem.getAnswerToClient ();
            answerToClient.setReqRes (req , res);
            l.n (strIdGroups , Domain.RNChat.RNGroups.Security.RN_SECURITY_GROUP_MEMBERS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("blocked by system") , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
        }
        return answerToClient;
    }

    public enum ValAnswer
    {
        group_not_found, no_members_found, you_are_not_a_member_of_the_group, id_members
    }
}
