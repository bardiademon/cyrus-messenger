package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.OtherUsers.Profile.General.GetInfoProfile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Get.General.GetGeneral;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.OtherUsers.Profile.General.ShowProfile.ShowProfile;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel.CheckUserAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = RouterName.RNChat.RNOtherUsers.RN_GET_INFO_PROFILE, method = RequestMethod.POST)
public final class GetInfoProfile
{
    private AnswerToClient answerToClient;
    private CheckUserAccessLevel accessLevel;

    private ShowProfile showProfile;

    @Autowired
    public GetInfoProfile (UserLoginService _UserLoginService , MainAccountService _MainAccountService , ShowProfileForService _ShowProfileForService , UserContactsService _UserContactsService , UserFriendsService _UserFriendsService , SecurityUserProfileService _SecurityUserProfileService , UserBlockedService _UserBlockedService)
    {
        showProfile = new ShowProfile (_UserLoginService , _MainAccountService , _ShowProfileForService , _UserContactsService , _UserFriendsService , _SecurityUserProfileService , _UserBlockedService);
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient get
            (HttpServletResponse res ,
             @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestGetInfoProfile request)
    {
        if (request == null)
            answerToClient = AnswerToClient.RequestIsNull ();
        else
        {
            answerToClient = showProfile.showProfile (res , codeLogin , request.getIdUser ());

            if (answerToClient.isOk () && answerToClient.getMessage ().containsKey (ShowProfile.KeyAnswer.i_can.name ()) && ((boolean) answerToClient.getMessage ().get (ShowProfile.KeyAnswer.i_can.name ())))
            {
                answerToClient = AnswerToClient.OK ();
                accessLevel = new CheckUserAccessLevel
                        (showProfile.getCheckLogin ().getVCodeLogin ().getMainAccount () , showProfile.getMainAccountGetProfile () , showProfile.getMainAccountService ());

                accessLevel.setServiceProfile (showProfile.getServiceProfile ());
                checkGet (request);
            }
        }

        return answerToClient;
    }

    private void checkGet (RequestGetInfoProfile request)
    {
        if (request.isGetName () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_name))
            answerToClient.put (GetGeneral.KeyAnswer.name.name () , showProfile.getMainAccountGetProfile ().getName ());

        if (request.isGetFamily () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_family))
            answerToClient.put (GetGeneral.KeyAnswer.family.name () , showProfile.getMainAccountGetProfile ().getFamily ());

        if (request.isGetMyLink () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_mylink))
            answerToClient.put (GetGeneral.KeyAnswer.mylink.name () , showProfile.getMainAccountGetProfile ().getMyLink ());

        if (request.isGetEmail () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_email))
            answerToClient.put (GetGeneral.KeyAnswer.email.name () , showProfile.getMainAccountGetProfile ().getEmail ());

        if (request.isGetPhone () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_phone))
            answerToClient.put (GetGeneral.KeyAnswer.phone.name () , showProfile.getMainAccountGetProfile ().getPhone ());

        if (request.isGetUsername () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_username))
            answerToClient.put (GetGeneral.KeyAnswer.username.name () , showProfile.getMainAccountGetProfile ().getUsername ());

        if (request.isGetBio () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.bio))
            answerToClient.put (GetGeneral.KeyAnswer.bio.name () , showProfile.getMainAccountGetProfile ().getBio ());
    }

}
