package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.RestUserList.RestAddUserList;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserList;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserListService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserListType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
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
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNUserList.RN_USER_LIST_ADD, method = RequestMethod.POST)
public final class RestAddUserList
{
    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;
    private final UserListService userListService;

    @Autowired
    public RestAddUserList
            (UserLoginService _UserLoginService ,
             MainAccountService _MainAccountService , UserListService _UserListService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.userListService = _UserListService;
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient add
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestAddUserList request)
    {
        AnswerToClient answerToClient = null;

        String router = Domain.RNChat.RNInfoUser.RNUserList.RN_USER_LIST_ADD;
        SubmitRequestType type = SubmitRequestType.add_user_list;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                IdUsernameMainAccount idUsernameMainAccount = new IdUsernameMainAccount (mainAccountService , request.getIdUsername ().getId () , request.getIdUsername ().getUsername ());
                if (idUsernameMainAccount.isValid ())
                {
                    if (idUsernameMainAccount.getIdUser () != mainAccount.getId ())
                    {
                        UserListType userListType = UserListType.to (request.getType ());
                        if (userListType != null)
                        {
                            UserList userList = userListService.getUserList (mainAccount.getId () , idUsernameMainAccount.getIdUser () , userListType);
                            if (userList == null)
                            {
                                userList = userListService.getUserListNot (mainAccount.getId () , idUsernameMainAccount.getIdUser () , userListType);

                                boolean ok = false;

                                if (userList != null)
                                {
                                    if (request.isChange ())
                                    {
                                        userList.setDeleted (true);
                                        userList.setDeletedAt (LocalDateTime.now ());
                                        userListService.Repository.save (userList);
                                        ok = true;
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.exists_other_type.name ());
                                        answerToClient.put (KeyAnswer.type.name () , userList.getType ().name ());
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.exists_other_type.name ()) , null);
                                        r.n (mainAccount , type , true);
                                    }
                                }
                                else ok = true;

                                if (ok)
                                {
                                    userListService.add (mainAccount , idUsernameMainAccount.getMainAccount () , userListType);
                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.added.name ());
                                    answerToClient.setReqRes (req , res);
                                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.added.name ());
                                    r.n (mainAccount , type , false);
                                }
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , AnswerToClient.CUV.found.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.found.name ()) , null);
                                r.n (mainAccount , type , true);
                            }
                        }
                        else
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.type_invalid.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.type_invalid.name ()) , null);
                            r.n (mainAccount , type , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.you_cannot_put_yourself_in_the_list.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_cannot_put_yourself_in_the_list.name ()) , null);
                        r.n (mainAccount , type , true);
                    }
                }
                else
                {
                    answerToClient = idUsernameMainAccount.getAnswerToClient ();
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (IdUsernameMainAccount.class.getName ()) , null);
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
        you_cannot_put_yourself_in_the_list, type_invalid, exists_other_type, added
    }

    private enum KeyAnswer
    {
        type
    }
}
