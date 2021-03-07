package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.OtherUsers.Profile.General.ShowProfile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNGap.RNOtherUsers.RN_SHOW_PROFILE, method = RequestMethod.POST)
public final class ShowProfile
{

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;

    @Autowired
    public ShowProfile (MainAccountService _MainAccountService , UserLoginService _UserLoginService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient showProfile
            (HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestParam (value = "id_user", defaultValue = "0", required = false) long idUser ,
             @RequestParam (value = "username", defaultValue = "", required = false) String username)
    {
        AnswerToClient answerToClient;

        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
        {
            IdUsernameMainAccount idUsernameMainAccount = new IdUsernameMainAccount (mainAccountService , idUser , username);
            if (idUsernameMainAccount.isValid ())
            {
                MainAccount mainAccountGetProfile = idUsernameMainAccount.getMainAccount ();

                UserProfileAccessLevel accessLevel = new UserProfileAccessLevel
                        (isLogin.getVCodeLogin ().getMainAccount () , mainAccountGetProfile);

                answerToClient = AnswerToClient.KeyAnswer (AnswerToClient.OK () ,
                        KeyAnswer.i_can , accessLevel.hasAccess (Which.profile));

            }
            else answerToClient = idUsernameMainAccount.getAnswerToClient ();
        }
        else answerToClient = isLogin.getAnswerToClient ();

        answerToClient.setResponse (res);

        return answerToClient;
    }

    public enum KeyAnswer
    {
        i_can
    }
}
