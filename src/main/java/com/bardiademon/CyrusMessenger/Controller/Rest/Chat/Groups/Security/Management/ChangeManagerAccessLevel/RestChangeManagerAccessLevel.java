package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.Security.Management.ChangeManagerAccessLevel;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.Security.Management.AddManager.RequestNewManager;
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
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
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

@RestController
@RequestMapping (value = Domain.RNChat.RNGroups.Security.RN_SECURITY_CHANGE_MANAGER_ACCESS_LEVEL, method = RequestMethod.POST)
public final class RestChangeManagerAccessLevel
{

    private final UserLoginService userLoginService;
    private final GroupManagementAccessLevelService groupManagementAccessLevelService;

    private final ManageGroup.Service service;

    @Autowired
    public RestChangeManagerAccessLevel
            (UserLoginService _UserLoginService , GroupsService _GroupsService , GroupManagementService _GroupManagementService ,
             MainAccountService _MainAccountService , GroupManagementAccessLevelService _GroupManagementAccessLevelService)
    {
        this.userLoginService = _UserLoginService;
        this.groupManagementAccessLevelService = _GroupManagementAccessLevelService;
        service = new ManageGroup.Service (_MainAccountService , _GroupsService , _GroupManagementService);
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient change
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestNewManager request)
    {
        AnswerToClient answerToClient;

        String router = Domain.RNChat.RNGroups.Security.RN_SECURITY_CHANGE_MANAGER_ACCESS_LEVEL;
        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , SubmitRequestType.change_manager);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                if ((answerToClient = request.checkRequest ()) == null)
                {
                    if (request.getIdUser ().isValid ())
                    {
                        MainAccount mainAccountUser = service.mainAccountService.findId (request.getIdUser ().getId ());
                        if (mainAccountUser != null)
                        {
                            ManageGroup manageGroup = new ManageGroup (service , request.getIdGroup () , mainAccount , AccessLevel.change_management_access_level);
                            if (manageGroup.canManage ())
                            {
                                IsManager isManagerUser = new IsManager (mainAccountUser , service.groupManagementService);
                                isManagerUser.setILUGroup (manageGroup.getManager ().getIluGroup ());
                                if (isManagerUser.isManager ())
                                {
                                    if (mainAccount.getId () != mainAccountUser.getId ())
                                    {
                                        if (!isManagerUser.isOwner ())
                                        {
                                            answerToClient = change (request , isManagerUser.getGroupManagement ().getAccessLevel () , isManagerUser.getGroupManagement () , mainAccount , router);
                                            if (answerToClient == null)
                                            {
                                                answerToClient = AnswerToClient.ServerError ();
                                                answerToClient.setReqRes (req , res);
                                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("Server error") , null);
                                            }
                                            else
                                            {
                                                answerToClient.setReqRes (req , res);
                                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.changed.name ());
                                                r.n (mainAccount , SubmitRequestType.change_manager , false);
                                            }
                                        }
                                        else
                                        {
                                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.this_user_owns_the_group.name ());
                                            answerToClient.setReqRes (req , res);
                                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_user_owns_the_group.name ()) , null);
                                            r.n (mainAccount , SubmitRequestType.change_manager , true);
                                        }
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.this_id_belongs_to_you.name ());
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_id_belongs_to_you.name ()) , null);
                                        r.n (mainAccount , SubmitRequestType.change_manager , true);
                                    }
                                }
                                else
                                {
                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.this_user_is_not_a_manager.name ());
                                    answerToClient.setReqRes (req , res);
                                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_user_is_not_a_manager.name ()) , null);
                                    r.n (mainAccount , SubmitRequestType.change_manager , true);
                                }
                            }
                            else answerToClient = manageGroup.getAnswerToClient ();
                        }
                        else
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.id_user_not_found.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_user_not_found.name ()) , null);
                            r.n (mainAccount , SubmitRequestType.change_manager , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_user_invalid.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_user_invalid.name ()) , null);
                        r.n (mainAccount , SubmitRequestType.change_manager , true);
                    }
                } // get answer from request.checkRequest ();
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_user_invalid.name ()) , null);
                r.n (mainAccount , SubmitRequestType.change_manager , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private AnswerToClient change (RequestNewManager request , GroupManagementAccessLevel accessLevelUser , GroupManagement groupManagement , MainAccount mainAccount , String router)
    {
        try
        {
            if (!Str.IsEmpty (request.getAddAdmin ()))
                accessLevelUser.setAddMember (Str.RealBool (request.getAddAdmin ()));

            if (!Str.IsEmpty (request.getChangeBio ()))
                accessLevelUser.setChangeBio (Str.RealBool (request.getChangeBio ()));

            if (!Str.IsEmpty (request.getChangeDescription ()))
                accessLevelUser.setChangeDescription (Str.RealBool (request.getChangeDescription ()));

            if (!Str.IsEmpty (request.getChangeLink ()))
                accessLevelUser.setChangeLink (Str.RealBool (request.getChangeLink ()));

            if (!Str.IsEmpty (request.getChangeManagementAccessLevel ()))
                accessLevelUser.setChangeManagementAccessLevel (Str.RealBool (request.getChangeManagementAccessLevel ()));

            if (!Str.IsEmpty (request.getChangeNameGroup ()))
                accessLevelUser.setChangeNameGroup (Str.RealBool (request.getChangeNameGroup ()));

            if (!Str.IsEmpty (request.getChangePicture ()))
                accessLevelUser.setChangePicture (Str.RealBool (request.getChangePicture ()));

            if (!Str.IsEmpty (request.getDelMainPic ()))
                accessLevelUser.setDelMainPic (Str.RealBool (request.getDelMainPic ()));

            if (!Str.IsEmpty (request.getDismissUser ()))
                accessLevelUser.setDismissUser (Str.RealBool (request.getDismissUser ()));

            if (!Str.IsEmpty (request.getDelMessageUser ()))
                accessLevelUser.setDelMessageUser (Str.RealBool (request.getDelMessageUser ()));

            if (!Str.IsEmpty (request.getDelPicture ()))
                accessLevelUser.setDelPicture (Str.RealBool (request.getDelPicture ()));

            if (!Str.IsEmpty (request.getSetMainPicture ()))
                accessLevelUser.setSetMainPicture (Str.RealBool (request.getSetMainPicture ()));

            if (!Str.IsEmpty (request.getShowListMember ()))
                accessLevelUser.setShowListMember (Str.RealBool (request.getShowListMember ()));

            if (!Str.IsEmpty (request.getTemporarilyClosed ()))
                accessLevelUser.setTemporarilyClosed (Str.RealBool (request.getTemporarilyClosed ()));

            if (!Str.IsEmpty (request.getShowMemberHidden ()))
                accessLevelUser.setShowMemberHidden (Str.RealBool (request.getShowMemberHidden ()));

            if (!Str.IsEmpty (request.getUploadPicture ()))
                accessLevelUser.setUploadPicture (Str.RealBool (request.getUploadPicture ()));

            if (!Str.IsEmpty (request.getAddMember ()))
                accessLevelUser.setAddMember (Str.RealBool (request.getAddMember ()));

            if (!Str.IsEmpty (request.getSendMessage ()))
                accessLevelUser.setSendMessage (Str.RealBool (request.getSendMessage ()));

            groupManagementAccessLevelService.Repository.save (accessLevelUser);

            if (!request.getName ().equals (RequestNewManager.DO_NOT_SET))
            {
                groupManagement.setName (request.getName ());
                service.groupManagementService.Repository.save (groupManagement);
            }

            return AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.changed.name ());
        }
        catch (Exception e)
        {
            l.n (null , router , mainAccount , null , Thread.currentThread ().getStackTrace () , e , null);
            return null;
        }
    }

    private enum ValAnswer
    {
        id_user_invalid, id_user_not_found, this_user_is_not_a_manager, this_user_owns_the_group, changed, this_id_belongs_to_you
    }
}
