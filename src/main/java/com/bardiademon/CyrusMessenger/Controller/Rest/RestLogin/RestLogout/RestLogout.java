package com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.RestLogout;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = Domain.RNLogin.RN_LOGOUT, method = RequestMethod.POST)
public final class RestLogout
{

    private UserLoginService userLoginService;

    public RestLogout (UserLoginService _UserLoginService)
    {
        this.userLoginService = _UserLoginService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient logout (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
                                  HttpServletResponse res , HttpServletRequest req)
    {
        AnswerToClient answerToClient;

        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
        {
            boolean logout;
            logout = userLoginService.logout (codeLogin);
            answerToClient = AnswerToClient.OneAnswer ((logout ? AnswerToClient.OK () : AnswerToClient.ServerError ()) , KeyAnswer.logout.name () , logout);

            MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();

            answerToClient.setReqRes (req , res);

            if (logout)
            {
                res.addCookie (MCookie.CookieApi (null));
                l.n (null , Domain.RNLogin.RN_LOGOUT , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () ,
                        null ,
                        ((new ToJson.CreateClass ()).put (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin)).toJson ());
            }
            else
            {
                l.n (null , Domain.RNLogin.RN_LOGOUT , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () ,
                        new Exception ("Can not logout") ,
                        ((new ToJson.CreateClass ()).put (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin)).toJson ());
            }
        }
        else
        {
            answerToClient = isLogin.getAnswerToClient ();
            answerToClient.setReqRes (req , res);

            l.n (null , Domain.RNLogin.RN_LOGOUT , null , answerToClient , Thread.currentThread ().getStackTrace () ,
                    new Exception ("Error from CheckLogin") ,
                    ((new ToJson.CreateClass ()).put (MCookie.KEY_CODE_LOGIN_COOKIE , codeLogin)).toJson ());
        }
        return answerToClient;
    }

    public enum KeyAnswer
    {
        logout
    }

}
