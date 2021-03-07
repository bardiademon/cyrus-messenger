package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Groups.Security.Management.Change.ChangeInfoGroup;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.ManageGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.ILUGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = Domain.RNGap.RNGroups.RN_UPDATE_INFO_GROUP, method = RequestMethod.POST)
public final class RestChangeInfoGroup
{
    private final UserLoginService userLoginService;
    private final ManageGroup.Service service;

    @Autowired
    public RestChangeInfoGroup
            (UserLoginService _UserLoginService ,
             MainAccountService _MainAccountService ,
             GroupsService _GroupsService ,
             GroupManagementService _GroupManagementService)
    {
        this.userLoginService = _UserLoginService;
        this.service = new ManageGroup.Service (_MainAccountService , _GroupsService , _GroupManagementService);
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient change
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @RequestBody RequestChangeInfoGroup request)
    {
        AnswerToClient answerToClient = null;
        String router = Domain.RNGap.RNGroups.RN_UPDATE_INFO_GROUP;
        SubmitRequestType type = SubmitRequestType.update_info_group;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                ID idGroup = request.getIdGroup ();
                if (idGroup != null && idGroup.isValid ())
                {
                    ILUGroup iluGroup = new ILUGroup (service.groupsService);
                    iluGroup.setId (idGroup.getId ());
                    if (iluGroup.isValid ())
                    {
                        Groups group = iluGroup.getGroup ();
                        assert group != null;

                        boolean updatedOne = false;
                        boolean error = false;

                        ManageGroup manageGroup;
                        if (!Str.IsEmpty (request.getDescription ()))
                        {
                            manageGroup = new ManageGroup (service , idGroup , mainAccount , AccessLevel.change_description);
                            if (!manageGroup.canManage ())
                            {
                                answerToClient = manageGroup.getAnswerToClient ();
                                answerToClient.put (Which.which , Which.description);
                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (Which.description));
                                r.n (mainAccount , type , true);
                                error = true;
                            }
                            else
                            {
                                group.setDescription (request.getDescription ());
                                updatedOne = true;
                            }
                        }
                        if (!error && !Str.IsEmpty (request.getBio ()))
                        {
                            manageGroup = new ManageGroup (service , idGroup , mainAccount , AccessLevel.change_bio);
                            if (!manageGroup.canManage ())
                            {
                                answerToClient = manageGroup.getAnswerToClient ();
                                answerToClient.put (Which.which , Which.bio);
                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (Which.bio));
                                r.n (mainAccount , type , true);
                                error = true;
                            }
                            else
                            {
                                group.setBio (request.getBio ());
                                updatedOne = true;
                            }
                        }
                        if (!error && !Str.IsEmpty (request.getLink ()))
                        {
                            manageGroup = new ManageGroup (service , idGroup , mainAccount , AccessLevel.change_link);
                            if (!manageGroup.canManage ())
                            {
                                answerToClient = manageGroup.getAnswerToClient ();
                                answerToClient.put (Which.which , Which.link);
                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (Which.link));
                                r.n (mainAccount , type , true);
                                error = true;
                            }
                            else
                            {
                                group.setLink (request.getLink ());
                                updatedOne = true;
                            }
                        }
                        if (!error && !Str.IsEmpty (request.getName ()))
                        {
                            manageGroup = new ManageGroup (service , idGroup , mainAccount , AccessLevel.change_name_group);
                            if (!manageGroup.canManage ())
                            {
                                answerToClient = manageGroup.getAnswerToClient ();
                                answerToClient.put (Which.which , Which.name);
                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (Which.name));
                                r.n (mainAccount , type , true);
                                error = true;
                            }
                            else
                            {
                                group.setName (request.getName ());
                                updatedOne = true;
                            }
                        }
                        if (!error && updatedOne)
                        {
                            service.groupsService.Repository.save (group);
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.changed);
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.changed);
                            r.n (mainAccount , type , false);
                        }

                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.group_not_found);
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.group_not_found));
                        r.n (mainAccount , type , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid ();
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.id_invalid));
                    r.n (mainAccount , type , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.request_is_null));
                r.n (mainAccount , type , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum Which
    {
        which,
        description, bio, name, username, link
    }

    private enum ValAnswer
    {
        group_not_found, changed
    }
}
