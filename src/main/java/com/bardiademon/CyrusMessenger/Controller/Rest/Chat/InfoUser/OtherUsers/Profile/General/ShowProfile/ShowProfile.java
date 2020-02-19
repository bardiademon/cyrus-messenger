package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.OtherUsers.Profile.General.ShowProfile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel.CheckUserAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = Domain.RNChat.RNOtherUsers.RN_SHOW_PROFILE, method = RequestMethod.POST)
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
             SecurityUserProfileService _SecurityUserProfileService ,
             UserBlockedService _UserBlockedService
            )
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        serviceProfile = new CheckUserAccessLevel.ServiceProfile (_ShowProfileForService , _UserContactsService , _UserFriendsService , _SecurityUserProfileService , _UserBlockedService);
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient showProfile
            (HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestParam (value = "id_user", defaultValue = "0", required = false) long idUser ,
             @RequestParam (value = "username", defaultValue = "", required = false) String username)
    {
        AnswerToClient answerToClient;

        checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
        if (checkLogin.isValid ())
        {
            IdUsernameMainAccount idUsernameMainAccount = new IdUsernameMainAccount (mainAccountService , idUser , username);
            if (idUsernameMainAccount.isValid ())
            {
                mainAccountGetProfile = idUsernameMainAccount.getMainAccount ();

                CheckUserAccessLevel accessLevel = new CheckUserAccessLevel
                        (checkLogin.getVCodeLogin ().getMainAccount () , mainAccountGetProfile , mainAccountService);

                accessLevel.setCheckProfile (CheckUserAccessLevel.CheckProfile.show_profile);
                accessLevel.setServiceProfile (serviceProfile);
                answerToClient = AnswerToClient.KeyAnswer (AnswerToClient.OK () ,
                        KeyAnswer.i_can.name () , accessLevel.check (accessLevel.CHK_PROFILE));

            }
            else answerToClient = idUsernameMainAccount.getAnswerToClient ();
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
        id_invalid, id_not_found, username_invalid
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
