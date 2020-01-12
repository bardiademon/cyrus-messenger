package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.GetInfoUser;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.VCodeLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping (value = "/api/info_user", method = RequestMethod.POST)
public class InfoUser
{

    private UserLoginService userLoginService;

    @Autowired
    public InfoUser (UserLoginService _UserLoginService)
    {
        this.userLoginService = _UserLoginService;
    }

    @RequestMapping ({"/" , ""})
    public AnswerToClient getInfoUser (HttpServletResponse res , @RequestBody RequestInfoUser requestInfoUser ,
                                       @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;
        if (codeLogin.equals ("")) answerToClient = AnswerToClient.NotLoggedIn ();
        else
        {
            VCodeLogin vCodeLogin = new VCodeLogin ();
            if (!vCodeLogin.IsValid (userLoginService.Repository , codeLogin))
                answerToClient = AnswerToClient.NotLoggedIn ();
            else
            {
                if (requestInfoUser.atLeastOne ())
                {
                    MainAccount mainAccount = vCodeLogin.getMainAccount ();

                    answerToClient = new AnswerToClient (200 , true);

                    if (requestInfoUser.isGetName ())
                        answerToClient.put (KeyAnswer.name.name () , mainAccount.getName ());

                    if (requestInfoUser.isGetFamily ())
                        answerToClient.put (KeyAnswer.family.name () , mainAccount.getFamily ());

                    if (requestInfoUser.isGetUsername ())
                        answerToClient.put (KeyAnswer.username.name () , mainAccount.getUsername ());

                    if (requestInfoUser.isGetEmail ())
                        answerToClient.put (KeyAnswer.email.name () , mainAccount.getEmail ());

                    if (requestInfoUser.isGetPhone ())
                        answerToClient.put (KeyAnswer.phone.name () , mainAccount.getPhone ());

                    if (requestInfoUser.isGetMyLink ())
                        answerToClient.put (KeyAnswer.mylink.name () , mainAccount.getMyLink ());

                    if (requestInfoUser.isGetBio ())
                        answerToClient.put (KeyAnswer.bio.name () , mainAccount.getBio ());
                }
                else answerToClient = AnswerToClient.error400 ();
            }
        }
        answerToClient.setResponse (res);
        return answerToClient;
    }

    private enum KeyAnswer
    {
        name, family, username, email, phone,
        bio, mylink
    }
}
