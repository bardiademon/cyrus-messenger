package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Groups.Security.Management.AddManager;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagement;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.IsManager;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagementAccessLevel.GroupManagementAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagementAccessLevel.GroupManagementAccessLevelService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.ManageGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.IsJoined;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroupService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = Domain.RNGap.RNGroups.Security.RN_SECURITY_NEW_MANAGER, method = RequestMethod.POST)
public final class RestNewManager
{

    private final UserLoginService userLoginService;
    private final JoinGroupService joinGroupService;
    private final GroupManagementAccessLevelService groupManagementAccessLevelService;

    private final ManageGroup.Service service;

    @Autowired
    public RestNewManager
            (UserLoginService _UserLoginService ,
             GroupsService _GroupsService , GroupManagementService _GroupManagementService ,
             MainAccountService _MainAccountService , JoinGroupService _JoinGroupService ,
             GroupManagementAccessLevelService _GroupManagementAccessLevelService)
    {
        this.userLoginService = _UserLoginService;
        this.joinGroupService = _JoinGroupService;
        this.groupManagementAccessLevelService = _GroupManagementAccessLevelService;
        service = new ManageGroup.Service (_MainAccountService , _GroupsService , _GroupManagementService);
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient newManager
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestNewManager request)
    {
        AnswerToClient answerToClient;
        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , Domain.RNGap.RNGroups.Security.RN_SECURITY_NEW_MANAGER , SubmitRequestType.new_manager);
        if (both.isOk ())
        {
            String cookieJson = ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ();
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                if ((answerToClient = request.checkRequest ()) == null)
                {
                    ManageGroup manageGroup = new ManageGroup (service , request.getIdGroup () , mainAccount , AccessLevel.add_admin);
                    if (manageGroup.canManage ())
                    {
                        ID idUser = request.getIdUser ();
                        if (idUser.isValid ())
                        {
                            MainAccount mainAccountUser = service.mainAccountService.findId (idUser.getId ());
                            if (mainAccountUser != null)
                            {
                                if (mainAccount.getId () != mainAccountUser.getId ())
                                {
                                    IsManager manager = manageGroup.getManager ();

                                    IsJoined isJoined = new IsJoined (joinGroupService , mainAccountUser , new ID (manager.getGroup ().getId ()));
                                    if (isJoined.is ())
                                    {
                                        IsManager isManagerUser = new IsManager (mainAccountUser , service.groupManagementService);
                                        isManagerUser.setILUGroup (manageGroup.getManager ().getIluGroup ());
                                        if (!isManagerUser.isManager ())
                                            answerToClient = addManagement (manager.getGroup () , manager.getMainAccount () , mainAccountUser , manager.getGroupManagement () , request);
                                        else
                                        {
                                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.this_user_is_manager.name ());
                                            answerToClient.setReqRes (req , res);
                                            l.n (ToJson.To (request) , Domain.RNGap.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_user_is_manager.name ()) , cookieJson);
                                            r.n (mainAccount , SubmitRequestType.group_members , true);
                                        }
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.this_user_is_not_member_of_group.name ());
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , Domain.RNGap.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_user_is_not_member_of_group.name ()) , cookieJson);
                                        r.n (mainAccount , SubmitRequestType.group_members , true);
                                    }

                                }
                                else
                                {
                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.this_id_belongs_to_you.name ());
                                    answerToClient.setReqRes (req , res);
                                    l.n (ToJson.To (request) , Domain.RNGap.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_id_belongs_to_you.name ()) , cookieJson);
                                    r.n (mainAccount , SubmitRequestType.group_members , true);
                                }
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.user_not_found.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (ToJson.To (request) , Domain.RNGap.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.user_not_found.name ()) , cookieJson);
                                r.n (mainAccount , SubmitRequestType.group_members , true);
                            }
                        }
                        else
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.id_user_invalid.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , Domain.RNGap.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("Error from CanManageGroup") , cookieJson);
                            r.n (mainAccount , SubmitRequestType.group_members , true);
                        }
                    }
                    else
                    {
                        answerToClient = manageGroup.getAnswerToClient ();
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , Domain.RNGap.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("Error from CanManageGroup") , cookieJson);
                        r.n (mainAccount , SubmitRequestType.group_members , true);
                    }

                }
                else
                {
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , Domain.RNGap.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_request.name ()) , cookieJson);
                    r.n (mainAccount , SubmitRequestType.group_members , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , Domain.RNGap.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_group_invalid.name ()) , cookieJson);
                r.n (mainAccount , SubmitRequestType.group_members , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private AnswerToClient addManagement (Groups group , MainAccount manager , MainAccount user , GroupManagement groupManagement , RequestNewManager request)
    {
        try
        {
            boolean update = groupManagement != null;

            GroupManagementAccessLevel accessLevel;

            if (!update) accessLevel = new GroupManagementAccessLevel ();
            else accessLevel = groupManagement.getAccessLevel ();

            accessLevel.setAddAdmin (toBool (request.getAddAdmin ()));
            accessLevel.setChangeBio (toBool (request.getChangeBio ()));
            accessLevel.setChangeDescription (toBool (request.getChangeDescription ()));
            accessLevel.setChangeLink (toBool (request.getChangeLink ()));
            accessLevel.setDelPicture (toBool (request.getDelPicture ()));
            accessLevel.setChangePicture (toBool (request.getChangePicture ()));
            accessLevel.setChangeManagementAccessLevel (toBool (request.getChangeManagementAccessLevel ()));
            accessLevel.setDelMainPic (toBool (request.getDelMainPic ()));
            accessLevel.setSetMainPicture (toBool (request.getSetMainPicture ()));
            accessLevel.setUploadPicture (toBool (request.getUploadPicture ()));
            accessLevel.setShowListMember (toBool (request.getShowListMember ()));
            accessLevel.setShowMemberHidden (toBool (request.getShowMemberHidden ()));
            accessLevel.setTemporarilyClosed (toBool (request.getTemporarilyClosed ()));
            accessLevel.setDismissUser (toBool (request.getDismissUser ()));
            accessLevel.setDelAdmin (toBool (request.getDelAdmin ()));
            accessLevel.setChangeNameGroup (toBool (request.getChangeNameGroup ()));
            accessLevel.setAddMember (toBool (request.getAddMember ()));
            accessLevel.setSendMessage (toBool (request.getSendMessage ()));
            accessLevel = groupManagementAccessLevelService.Repository.save (accessLevel);

            if (!update)
            {
                groupManagement = new GroupManagement ();
                groupManagement.setName (request.getName ());
                groupManagement.setMainAccount (user);
                groupManagement.setManagedBy (manager);
                groupManagement.setGroups (group);
                groupManagement.setAccessLevel (accessLevel);
                groupManagement = service.groupManagementService.Repository.save (groupManagement);
            }

            if (!update && !request.getName ().equals ("DO_NOT_SET"))
            {
                groupManagement.setName (request.getName ());
                groupManagement = service.groupManagementService.Repository.save (groupManagement);
            }

            if (!update)
            {
                accessLevel.setGroupManagement (groupManagement);
                groupManagementAccessLevelService.Repository.save (accessLevel);
            }

            if (update) return AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.update_manager.name ());
            else return AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.added_manager.name ());
        }
        catch (Exception e)
        {
            AnswerToClient answerToClient = AnswerToClient.ServerError ();
            l.n (ToJson.To (request) , Domain.RNGap.RNGroups.Security.RN_SECURITY_NEW_MANAGER , manager , answerToClient , Thread.currentThread ().getStackTrace () , e , null);
            return answerToClient;
        }
    }

    private boolean toBool (String str)
    {
        Boolean toBool = Str.ToBool (str);
        if (toBool == null) toBool = false;
        return toBool;
    }

    public enum ValAnswer
    {
        user_not_found, id_group_invalid,
        id_user_invalid, this_user_is_manager,
        invalid_request, added_manager, update_manager, this_id_belongs_to_you, this_user_is_not_member_of_group
    }
}
