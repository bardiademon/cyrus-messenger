package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.OtherUsers.Profile.General.GetInfoProfile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Get.General.GetGeneral;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel.CheckUserAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = Domain.RNChat.RNOtherUsers.RN_GET_INFO_PROFILE, method = RequestMethod.POST)
public final class RestGetInfoProfile
{
    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;

    private final CheckUserAccessLevel.ServiceProfile serviceProfile;

    @Autowired
    public RestGetInfoProfile
            (UserLoginService _UserLoginService ,
             MainAccountService _MainAccountService ,
             UserContactsService _UserContactsService ,
             UserFriendsService _UserFriendsService ,
             SecurityUserProfileService _SecurityUserProfileService ,
             UserBlockedService _UserBlockedService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.serviceProfile = new CheckUserAccessLevel.ServiceProfile
                (mainAccountService.showProfileForService , _UserContactsService , _UserFriendsService , _SecurityUserProfileService , _UserBlockedService);
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient get
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestGetInfoProfile request)
    {
        AnswerToClient answerToClient;
        String router = Domain.RNChat.RNOtherUsers.RN_GET_INFO_PROFILE;
        SubmitRequestType type = SubmitRequestType.get_info_profile;
        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            if (request != null && (request.getIdUser () > 0 || !Str.IsEmpty (request.getUsername ())))
            {
                IdUsernameMainAccount idUsernameMainAccount = new IdUsernameMainAccount (mainAccountService , request.getIdUser () , request.getUsername ());
                if (idUsernameMainAccount.isValid ())
                {
                    MainAccount mainAccountGetInfo = idUsernameMainAccount.getMainAccount ();
                    CheckUserAccessLevel accessLevel = new CheckUserAccessLevel (mainAccount , mainAccountGetInfo , mainAccountService);
                    accessLevel.setServiceProfile (serviceProfile);
                    if (accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_profile))
                    {
                        answerToClient = checkGet (request , accessLevel , mainAccountGetInfo);
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , null);
                        r.n (mainAccount , type , false);
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.disabled_profile_display.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (IdUsernameMainAccount.class.getName ()) , null);
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
                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null);
                r.n (mainAccount , type , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private AnswerToClient checkGet (RequestGetInfoProfile request , CheckUserAccessLevel accessLevel , MainAccount mainAccount)
    {
        AnswerToClient answerToClient = AnswerToClient.OK ();

        if (request.isGetId () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_id))
            answerToClient.put (GetGeneral.KeyAnswer.id.name () , mainAccount.getId ());

        if (request.isGetName () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_name))
            answerToClient.put (GetGeneral.KeyAnswer.name.name () , mainAccount.getName ());

        if (request.isGetFamily () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_family))
            answerToClient.put (GetGeneral.KeyAnswer.family.name () , mainAccount.getFamily ());

        if (request.isGetMyLink () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_mylink))
            answerToClient.put (GetGeneral.KeyAnswer.mylink.name () , mainAccount.getMyLink ());

        if (request.isGetEmail () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_email))
            answerToClient.put (GetGeneral.KeyAnswer.email.name () , mainAccount.getEmail ());

        if (request.isGetPhone () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_phone))
            answerToClient.put (GetGeneral.KeyAnswer.phone.name () , mainAccount.getPhone ());

        if (request.isGetUsername () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.show_username))
            answerToClient.put (GetGeneral.KeyAnswer.username.name () , mainAccount.getUsername ().getUsername ());

        if (request.isGetBio () && accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.bio))
            answerToClient.put (GetGeneral.KeyAnswer.bio.name () , mainAccount.getBio ());

        return answerToClient;
    }

    private enum ValAnswer
    {
        disabled_profile_display
    }

}
