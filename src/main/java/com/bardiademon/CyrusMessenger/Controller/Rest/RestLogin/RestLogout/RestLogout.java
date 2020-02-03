package com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.RestLogout;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
    public AnswerToClient logout (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin , HttpServletResponse res)
    {
        AnswerToClient answerToClient;

        CheckLogin checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
        if (checkLogin.isValid ())
        {
            boolean logout;
            logout = userLoginService.logout (codeLogin);
            answerToClient = AnswerToClient.OneAnswer ((logout ? AnswerToClient.OK () : AnswerToClient.ServerError ()) , KeyAnswer.logout.name () , logout);

            if (logout) res.addCookie (MCookie.CookieApi (null));
        }
        else answerToClient = checkLogin.getAnswerToClient ();

        answerToClient.setResponse (res);
        return answerToClient;
    }

    public enum KeyAnswer
    {
        logout
    }

}
