package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.New.General;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = Domain.RNChat.RNNewInfoUser.RN_GENERAL, method = RequestMethod.POST)
public final class NewGeneral
{

    private UserLoginService userLoginService;
    private MainAccountService mainAccountService;

    @Autowired
    public NewGeneral (UserLoginService _UserLoginService , MainAccountService _MainAccountService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping (value = {"/" , ""})
    private AnswerToClient newInfoUser (HttpServletResponse res , @RequestBody RequestGeneral requestGeneral ,
                                        @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        CheckLogin checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
        if (checkLogin.isValid ())
        {
            requestGeneral = mainAccountService.updateGeneral (checkLogin.getVCodeLogin ().getMainAccount () , requestGeneral);

            if (requestGeneral != null)
            {
                answerToClient = AnswerToClient.OK ();

                if (requestGeneral.isUpdatedBio () || !requestGeneral.isNull (requestGeneral.getBio ()))
                    answerToClient.put (KeyAnswer.bio.name () , requestGeneral.isUpdatedBio ());

                if (requestGeneral.isUpdatedName () || !requestGeneral.isNull (requestGeneral.getName ()))
                    answerToClient.put (KeyAnswer.name.name () , requestGeneral.isUpdatedName ());

                if (requestGeneral.isUpdatedFamily () || !requestGeneral.isNull (requestGeneral.getFamily ()))
                    answerToClient.put (KeyAnswer.family.name () , requestGeneral.isUpdatedFamily ());

                if (requestGeneral.isUpdatedUsername () || !requestGeneral.isNull (requestGeneral.getUsername ()))
                    answerToClient.put (KeyAnswer.username.name () , requestGeneral.isUpdatedUsername ());

                if (requestGeneral.isUpdatedMylink () || !requestGeneral.isNull (requestGeneral.getMylink ()))
                    answerToClient.put (KeyAnswer.mylink.name () , requestGeneral.isUpdatedMylink ());
            }
            else
                answerToClient = AnswerToClient.error400 ();
        }
        else answerToClient = checkLogin.getAnswerToClient ();

        answerToClient.setResponse (res);
        return answerToClient;
    }

    private enum KeyAnswer
    {
        bio, name, family, username, mylink
    }


}
