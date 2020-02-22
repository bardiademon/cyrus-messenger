package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.Security.Management.NewManager;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagement;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagementAccessLevel.GroupManagementAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagementAccessLevel.GroupManagementAccessLevelService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
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
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER, method = RequestMethod.POST)
public final class RestNewManager
{

    private final UserLoginService userLoginService;
    private final GroupsService groupsService;
    private final GroupManagementService groupManagementService;
    private final MainAccountService mainAccountService;
    private final JoinGroupService joinGroupService;
    private final GroupManagementAccessLevelService groupManagementAccessLevelService;

    @Autowired
    public RestNewManager
            (UserLoginService _UserLoginService ,
             GroupsService _GroupsService , GroupManagementService _GroupManagementService ,
             MainAccountService _MainAccountService , JoinGroupService _JoinGroupService ,
             GroupManagementAccessLevelService _GroupManagementAccessLevelService)
    {
        this.userLoginService = _UserLoginService;
        this.groupsService = _GroupsService;
        this.groupManagementService = _GroupManagementService;
        this.mainAccountService = _MainAccountService;
        this.joinGroupService = _JoinGroupService;
        this.groupManagementAccessLevelService = _GroupManagementAccessLevelService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient newManager
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestNewManager request)
    {
        AnswerToClient answerToClient;
        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                if ((answerToClient = checkRequest (request)) == null)
                {
                    ID idGroup = new ID (request.getIdGroup ());
                    if (idGroup.isValid ())
                    {
                        ID idUser = new ID (request.getIdUser ());
                        if (idUser.isValid ())
                        {
                            MainAccount mainAccountUser = mainAccountService.findId (idUser.getId ());
                            if (mainAccountUser != null)
                            {
                                if (mainAccount.getId () != mainAccountUser.getId ())
                                {
                                    Groups group = groupsService.hasGroup (idGroup.getId ());
                                    if (group != null)
                                    {
                                        GroupManagement groupManagement = groupManagementService.getGroupManagement (mainAccount.getId () , group.getId ());
                                        boolean isOwner = (group.getOwner ().getId () == mainAccount.getId ());
                                        if (isOwner || groupManagement != null)
                                        {
                                            if (group.getOwner ().getId () != mainAccountUser.getId ())
                                            {
                                                JoinGroup joined = joinGroupService.isJoined (group.getId () , mainAccountUser.getId ());
                                                if (joined != null)
                                                {

                                                    GroupManagement groupManagementUser = groupManagementService.getGroupManagement (mainAccountUser.getId () , group.getId ());
                                                    if (groupManagementUser == null)
                                                    {
                                                        boolean managementHasAccess = false;
                                                        if (!isOwner)
                                                        {
                                                            GroupManagementAccessLevel accessLevel = groupManagement.getAccessLevel ();
                                                            managementHasAccess = (accessLevel.isAddAdmin () || accessLevel.isChangeManagementAccessLevel ());
                                                        }
                                                        if (isOwner || managementHasAccess)
                                                        {
                                                            answerToClient = addManagement (group , mainAccount , mainAccountUser , request);
                                                            answerToClient.setReqRes (req , res);
                                                            l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.added_manager.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                                            r.n (mainAccount , SubmitRequestType.group_members , false);
                                                        }
                                                        else
                                                        {
                                                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.you_do_not_have_access.name ());
                                                            answerToClient.setReqRes (req , res);
                                                            l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_do_not_have_access.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                                            r.n (mainAccount , SubmitRequestType.group_members , true);
                                                        }
                                                    }
                                                    else
                                                    {
                                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.user_is_admin.name ());
                                                        answerToClient.setReqRes (req , res);
                                                        l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.user_is_admin.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                                        r.n (mainAccount , SubmitRequestType.group_members , true);
                                                    }


                                                }
                                                else
                                                {
                                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.user_is_not_a_member_of_the_group.name ());
                                                    answerToClient.setReqRes (req , res);
                                                    l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.user_is_not_a_member_of_the_group.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                                    r.n (mainAccount , SubmitRequestType.group_members , true);
                                                }
                                            }
                                            else
                                            {
                                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.this_id_belongs_to_the_group_owner.name ());
                                                answerToClient.setReqRes (req , res);
                                                l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_id_belongs_to_the_group_owner.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                                r.n (mainAccount , SubmitRequestType.group_members , true);
                                            }
                                        }
                                        else
                                        {
                                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.you_are_not_a_manager.name ());
                                            answerToClient.setReqRes (req , res);
                                            l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_are_not_a_manager.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                            r.n (mainAccount , SubmitRequestType.group_members , true);
                                        }
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.group_not_found.name ());
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.group_not_found.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                        r.n (mainAccount , SubmitRequestType.group_members , true);
                                    }
                                }
                                else
                                {
                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.this_id_belongs_to_you.name ());
                                    answerToClient.setReqRes (req , res);
                                    l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_id_belongs_to_you.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                    r.n (mainAccount , SubmitRequestType.group_members , true);
                                }
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.user_not_found.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.user_not_found.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                                r.n (mainAccount , SubmitRequestType.group_members , true);
                            }
                        }
                        else
                        {
                            answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_user_invalid.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_user_invalid.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                            r.n (mainAccount , SubmitRequestType.group_members , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_group_invalid.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_group_invalid.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                        r.n (mainAccount , SubmitRequestType.group_members , true);
                    }
                }
                else
                {
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_request.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                    r.n (mainAccount , SubmitRequestType.group_members , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_group_invalid.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin).toJson ());
                r.n (mainAccount , SubmitRequestType.group_members , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    @Nullable
    private AnswerToClient checkRequest (@NotNull RequestNewManager request)
    {
        if (!Str.IsEmpty (request.getAddAdmin ()) && !Str.HasBool (request.getAddAdmin ()))
            return notBool ("add_admin");
        else if (!Str.IsEmpty (request.getChangeBio ()) && !Str.HasBool (request.getChangeBio ()))
            return notBool ("change_bio");
        else if (!Str.IsEmpty (request.getChangeDescription ()) && !Str.HasBool (request.getChangeDescription ()))
            return notBool ("change_description");
        else if (!Str.IsEmpty (request.getChangeLink ()) && !Str.HasBool (request.getChangeLink ()))
            return notBool ("change_link");
        else if (!Str.IsEmpty (request.getChangeManagementAccessLevel ()) && !Str.HasBool (request.getChangeManagementAccessLevel ()))
            return notBool ("change_management_access_level");
        else if (!Str.IsEmpty (request.getChangeNameGroup ()) && !Str.HasBool (request.getChangeNameGroup ()))
            return notBool ("change_name_group");
        else if (!Str.IsEmpty (request.getChangePicture ()) && !Str.HasBool (request.getChangePicture ()))
            return notBool ("change_picture");
        else if (!Str.IsEmpty (request.getDelMainPic ()) && !Str.HasBool (request.getDelMainPic ()))
            return notBool ("del_main_pic");
        else if (!Str.IsEmpty (request.getDismissUser ()) && !Str.HasBool (request.getDismissUser ()))
            return notBool ("dismiss_user");
        else if (!Str.IsEmpty (request.getDelMessageUser ()) && !Str.HasBool (request.getDelMessageUser ()))
            return notBool ("del_message_user");
        else if (!Str.IsEmpty (request.getDelPicture ()) && !Str.HasBool (request.getDelPicture ()))
            return notBool ("del_picture");
        else if (!Str.IsEmpty (request.getSetMainPicture ()) && !Str.HasBool (request.getSetMainPicture ()))
            return notBool ("set_main_pic");
        else if (!Str.IsEmpty (request.getShowListMember ()) && !Str.HasBool (request.getShowListMember ()))
            return notBool ("show_list_member");
        else if (!Str.IsEmpty (request.getTemporarilyClosed ()) && !Str.HasBool (request.getTemporarilyClosed ()))
            return notBool ("temporarily_closed");
        else if (!Str.IsEmpty (request.getShowMemberHidden ()) && !Str.HasBool (request.getShowMemberHidden ()))
            return notBool ("show_member_hidden");
        else if (!Str.IsEmpty (request.getUploadPicture ()) && !Str.HasBool (request.getUploadPicture ()))
            return notBool ("upload_picture");
        else if (!Str.IsEmpty (request.getAddMember ()) && !Str.HasBool (request.getAddMember ()))
            return notBool ("add_member");
        else return null;
    }

    @NotNull
    private AnswerToClient notBool (String which)
    {
        AnswerToClient answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.invalid_request);
        answerToClient.put (AnswerToClient.CUK.which.name () , which);
        return answerToClient;
    }

    private AnswerToClient addManagement (Groups group , MainAccount manager , MainAccount user , RequestNewManager request)
    {
        try
        {
            GroupManagementAccessLevel accessLevel = new GroupManagementAccessLevel ();

            System.out.println (request.getAddAdmin ());
            System.out.println (Str.ToBool (request.getAddAdmin ()));
            System.out.println (toBool (request.getAddAdmin ()));

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
            accessLevel = groupManagementAccessLevelService.Repository.save (accessLevel);

            GroupManagement groupManagement = new GroupManagement ();
            groupManagement.setAccessLevel (accessLevel);
            groupManagement.setName (request.getName ());
            groupManagement.setMainAccount (user);
            groupManagement.setManagedBy (manager);
            groupManagement.setGroups (group);
            groupManagement = groupManagementService.Repository.save (groupManagement);

            accessLevel.setGroupManagement (groupManagement);
            groupManagementAccessLevelService.Repository.save (accessLevel);

            return AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.added_manager.name ());
        }
        catch (Exception e)
        {
            AnswerToClient answerToClient = AnswerToClient.ServerError ();
            l.n (ToJson.To (request) , Domain.RNChat.RNGroups.Security.RN_SECURITY_NEW_MANAGER , manager , answerToClient , Thread.currentThread ().getStackTrace () , e , null);
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
        group_not_found, user_not_found, id_group_invalid,
        id_user_invalid, you_are_not_a_manager, you_do_not_have_access, user_is_not_a_member_of_the_group,
        invalid_request, added_manager, this_id_belongs_to_you, user_is_admin, this_id_belongs_to_the_group_owner
    }
}
