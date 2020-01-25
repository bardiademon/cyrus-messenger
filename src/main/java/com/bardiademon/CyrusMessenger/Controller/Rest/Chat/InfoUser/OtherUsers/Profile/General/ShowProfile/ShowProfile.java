package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.OtherUsers.Profile.General.ShowProfile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = RouterName.RNChat.RNOtherUsers.RN_SHOW_PROFILE, method = RequestMethod.POST)
public final class ShowProfile
{

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;

    private final CheckUserAccessLevel.ServiceProfile serviceProfile;

    private CheckLogin checkLogin;
    private MainAccount mainAccountGetProfile;


    @Autowired
    public ShowProfile
            (UserLoginService _UserLoginService ,
             MainAccountService _MainAccountService ,
             ShowProfileForService _ShowProfileForService ,
             UserContactsService _UserContactsService ,
             UserFriendsService _UserFriendsService ,
             SecurityUserProfileService _SecurityUserProfileService
            )
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        serviceProfile = new CheckUserAccessLevel.ServiceProfile (_ShowProfileForService , _UserContactsService , _UserFriendsService , _SecurityUserProfileService);
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient showProfile
            (HttpServletResponse res ,
             @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestParam ("id_user") long idUser)
    {
        AnswerToClient answerToClient;

        checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
        if (checkLogin.isValid ())
        {
            if (idUser > 0)
            {
                mainAccountGetProfile = mainAccountService.findValidById (idUser);
                if (mainAccountGetProfile != null)
                {
                    CheckUserAccessLevel accessLevel = new CheckUserAccessLevel
                            (checkLogin.getVCodeLogin ().getMainAccount () , mainAccountGetProfile , mainAccountService);

                    accessLevel.setCheckProfile (CheckUserAccessLevel.CheckProfile.show_profile);
                    accessLevel.setServiceProfile (serviceProfile);
                    accessLevel.check (accessLevel.CHK_PROFILE);

                    answerToClient = AnswerToClient.KeyAnswer (AnswerToClient.OK () ,
                            KeyAnswer.i_can.name () , accessLevel.check (accessLevel.CHK_PROFILE));
                }
                else
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.id_not_found.name ());
            }
            else answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.id_invalid.name ());
        }
        else answerToClient = checkLogin.getAnswerToClient ();

        answerToClient.setResponse (res);

        return answerToClient;
    }

    public enum KeyAnswer
    {
        i_can
    }

    public enum ValAnswer
    {
        id_invalid, id_not_found
    }

    public CheckLogin getCheckLogin ()
    {
        return checkLogin;
    }

    public MainAccount getMainAccountGetProfile ()
    {
        return mainAccountGetProfile;
    }

    public CheckUserAccessLevel.ServiceProfile getServiceProfile ()
    {
        return serviceProfile;
    }

    public MainAccountService getMainAccountService ()
    {
        return mainAccountService;
    }

    public UserLoginService getUserLoginService ()
    {
        return userLoginService;
    }
}
