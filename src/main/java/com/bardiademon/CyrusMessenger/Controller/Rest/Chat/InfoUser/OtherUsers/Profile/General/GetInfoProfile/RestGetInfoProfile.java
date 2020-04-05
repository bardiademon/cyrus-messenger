package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.OtherUsers.Profile.General.GetInfoProfile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Get.General.GetGeneral;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserListService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfiles;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
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

    private final UserProfileAccessLevel.Service serviceProfile;

    @Autowired
    public RestGetInfoProfile (
            MainAccountService _MainAccountService ,
            EnumTypesService _EnumTypesService ,
            UserLoginService _UserLoginService ,
            UserListService _UserListService ,
            UserFriendsService _UserFriendsService ,
            UserContactsService _UserContactsService ,
            UserSeparateProfilesService _UserSeparateProfilesService ,
            UserBlockedService _UserBlockedService ,
            ProfilePicturesService _ProfilePicturesService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.serviceProfile = new UserProfileAccessLevel.Service (_MainAccountService ,
                _EnumTypesService ,
                _UserListService ,
                _UserFriendsService ,
                _UserContactsService ,
                _UserSeparateProfilesService ,
                _UserBlockedService ,
                _ProfilePicturesService);
    }

    @RequestMapping (value = { "" , "/" })
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
                    UserProfileAccessLevel accessLevel = new UserProfileAccessLevel (serviceProfile , mainAccount , mainAccountGetInfo);
                    if (accessLevel.hasAccess (Which.profile))
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

    private AnswerToClient checkGet (RequestGetInfoProfile request , UserProfileAccessLevel accessLevel , MainAccount mainAccount)
    {
        AnswerToClient answerToClient = AnswerToClient.OK ();

        if (request.isGetId () && accessLevel.hasAccess (Which.id))
            answerToClient.put (GetGeneral.KeyAnswer.id.name () , mainAccount.getId ());

        UserSeparateProfiles separateProfiles = null;
        if (accessLevel.isSeparateProfile ()) separateProfiles = accessLevel.getSeparateProfile ();

        if (request.isGetName () && accessLevel.hasAccess (Which.name))
        {
            if (accessLevel.isSeparateProfile ())
            {
                assert separateProfiles != null;
                answerToClient.put (GetGeneral.KeyAnswer.name.name () , separateProfiles.getName ());
            }
            else answerToClient.put (GetGeneral.KeyAnswer.name.name () , mainAccount.getName ());
        }

        if (request.isGetFamily () && accessLevel.hasAccess (Which.family))
        {
            if (accessLevel.isSeparateProfile ())
            {
                assert separateProfiles != null;
                answerToClient.put (GetGeneral.KeyAnswer.family.name () , separateProfiles.getFamily ());
            }
            else answerToClient.put (GetGeneral.KeyAnswer.family.name () , mainAccount.getFamily ());
        }

        if (request.isGetMyLink () && accessLevel.hasAccess (Which.mylink))
        {
            if (accessLevel.isSeparateProfile ())
            {
                assert separateProfiles != null;
                answerToClient.put (GetGeneral.KeyAnswer.mylink.name () , separateProfiles.getMylink ());
            }
            else answerToClient.put (GetGeneral.KeyAnswer.mylink.name () , mainAccount.getMyLink ());
        }

        if (request.isGetEmail () && accessLevel.hasAccess (Which.email))
        {
            if (accessLevel.isSeparateProfile ())
            {
                assert separateProfiles != null;
                answerToClient.put (GetGeneral.KeyAnswer.email.name () , separateProfiles.getEmail ());
            }
            else answerToClient.put (GetGeneral.KeyAnswer.email.name () , mainAccount.getEmail ());
        }

        if (request.isGetPhone () && accessLevel.hasAccess (Which.phone))
            answerToClient.put (GetGeneral.KeyAnswer.phone.name () , mainAccount.getPhone ());

        if (request.isGetUsername () && accessLevel.hasAccess (Which.username))
            answerToClient.put (GetGeneral.KeyAnswer.username.name () , mainAccount.getUsername ().getUsername ());

        if (request.isGetBio () && accessLevel.hasAccess (Which.bio))
        {
            if (accessLevel.isSeparateProfile ())
            {
                assert separateProfiles != null;
                answerToClient.put (GetGeneral.KeyAnswer.bio.name () , separateProfiles.getBio ());
            }
            else answerToClient.put (GetGeneral.KeyAnswer.bio.name () , mainAccount.getBio ());
        }

        return answerToClient;
    }

    private enum ValAnswer
    {
        disabled_profile_display
    }

}
