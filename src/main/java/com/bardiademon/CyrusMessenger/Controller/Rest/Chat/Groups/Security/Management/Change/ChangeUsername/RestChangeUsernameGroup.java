package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.Security.Management.Change.ChangeUsername;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.ManageGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernameFor;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.Usernames;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = Domain.RNChat.RNGroups.RN_CHANGE_USERNAME_GROUP, method = RequestMethod.POST)
public final class RestChangeUsernameGroup
{

    private final UserLoginService userLoginService;
    private final UsernamesService usernamesService;
    private final ManageGroup.Service service;

    @Autowired
    public RestChangeUsernameGroup
            (UserLoginService _UserLoginService ,
             MainAccountService _MainAccountService , GroupsService _GroupsService , GroupManagementService _GroupManagementService ,
             UsernamesService _UsernamesService)
    {
        this.userLoginService = _UserLoginService;
        this.usernamesService = _UsernamesService;
        this.service = new ManageGroup.Service (_MainAccountService , _GroupsService , _GroupManagementService);
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient change
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req , @RequestBody RequestChangeUsernameGroup request)
    {
        AnswerToClient answerToClient = null;

        String router = Domain.RNChat.RNGroups.RN_CHANGE_USERNAME_GROUP;
        SubmitRequestType type = SubmitRequestType.change_username_group;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            if (request != null && !Str.IsEmpty (request.getUsername ()))
            {
                VUsername vUsername = new VUsername (request.getUsername ());
                if (vUsername.check ())
                {
                    ID idGroup = request.getId ();
                    if (idGroup != null && idGroup.isValid ())
                    {
                        ManageGroup manageGroup = new ManageGroup (service , idGroup , mainAccount , AccessLevel.change_username);
                        if (manageGroup.canManage ())
                        {
                            Groups group = manageGroup.getManager ().getGroup ();

                            boolean change = false;

                            Usernames findUsernames = usernamesService.findForGroup (request.getUsername ());

                            if (findUsernames == null) change = true;
                            else
                            {
                                Groups findGroup = findUsernames.getGroups ();

                                if (findGroup.getId () == group.getId ())
                                {
                                    Usernames groupname = group.getGroupname ();
                                    Usernames findGroupname = findGroup.getGroupname ();
                                    if (findGroupname != null && findGroupname.getUsername ().equals (groupname.getUsername ()))
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.repetitive.name ());
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.repetitive.name ()) , null);
                                        r.n (mainAccount , type , true);
                                    }
                                    else change = true;
                                }
                                else
                                {
                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.this_username_used.name ());
                                    answerToClient.setReqRes (req , res);
                                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_username_used.name ()) , null);
                                    r.n (mainAccount , type , true);
                                }
                            }

                            if (change)
                            {
                                Usernames oldUsername = group.getGroupname ();

                                if (oldUsername != null)
                                {
                                    oldUsername.setDeleted (true);
                                    oldUsername.setDeletedAt (LocalDateTime.now ());
                                    oldUsername.setId2 (group.getId ());
                                    oldUsername.setGroups (null);

                                    usernamesService.Repository.save (oldUsername);
                                }

                                Usernames username = new Usernames ();
                                username.setGroups (group);
                                username.setUsername (request.getUsername ());
                                username.setUsernameFor (UsernameFor.group);
                                username = usernamesService.Repository.save (username);

                                group.setGroupname (username);
                                service.groupsService.Repository.save (group);

                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.changed.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.changed.name ());
                                r.n (mainAccount , type , false);
                            }
                        }
                        else
                        {
                            answerToClient = manageGroup.getAnswerToClient ();
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ManageGroup.class.getName ()) , null);
                            r.n (mainAccount , type , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.IdInvalid ();
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.id_invalid.name ()) , null);
                        r.n (mainAccount , type , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.username_invalid.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.username_invalid.name ()) , null);
                    r.n (mainAccount , type , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null);
                r.n (mainAccount , type , true);
            }

        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum ValAnswer
    {
        this_username_used, repetitive, changed
    }
}
