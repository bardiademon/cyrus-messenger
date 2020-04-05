package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.RestUserList.RestRemoveUserList;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Rest.UsedRequests.R_IDUsername;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserList;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserListService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNUserList.RN_USER_LIST_REMOVE, method = RequestMethod.POST)
public final class RestRemoveUserList
{
    private UserListService userListService;
    private UserLoginService userLoginService;
    private MainAccountService mainAccountService;

    @Autowired
    public RestRemoveUserList (UserListService _UserListService , UserLoginService _UserLoginService , MainAccountService _MainAccountService)
    {
        this.userListService = _UserListService;
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient remove
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody R_IDUsername request)
    {
        AnswerToClient answerToClient;

        String router = Domain.RNChat.RNInfoUser.RNUserList.RN_USER_LIST_REMOVE;
        SubmitRequestType type = SubmitRequestType.remove_user_list;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                IdUsernameMainAccount idUsernameMainAccount = new IdUsernameMainAccount (mainAccountService , request.getId () , request.getUsername ());
                if (idUsernameMainAccount.isValid ())
                {
                    UserList userList = userListService.getUserList (mainAccount.getId () , idUsernameMainAccount.getIdUser ());
                    if (userList != null)
                    {
                        userListService.remove (userList);
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.removed.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.removed.name ());
                        r.n (mainAccount , type , false);
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.this_user_is_not_in_your_list.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_user_is_not_in_your_list.name ()) , null);
                        r.n (mainAccount , type , true);
                    }
                }
                else
                {
                    answerToClient = idUsernameMainAccount.getAnswerToClient ();
                    answerToClient.setReqRes (req , res);
                    l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (IdUsernameMainAccount.class.getName ()) , null);
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

    public enum ValAnswer
    {
        this_user_is_not_in_your_list
    }
}
