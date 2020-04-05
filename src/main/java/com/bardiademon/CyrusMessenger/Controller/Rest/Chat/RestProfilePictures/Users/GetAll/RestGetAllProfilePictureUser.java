package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Users.GetAll;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserListService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.ProfilePictures.SortProfilePictures;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping (value = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ALL_USER, method = RequestMethod.POST)
public final class RestGetAllProfilePictureUser
{

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;
    private final UserProfileAccessLevel.Service serviceProfile;

    public RestGetAllProfilePictureUser
            (
                    MainAccountService _MainAccountService ,
                    EnumTypesService _EnumTypesService ,
                    UserLoginService _UserLoginService ,
                    UserListService _UserListService ,
                    UserFriendsService _UserFriendsService ,
                    UserContactsService _UserContactsService ,
                    UserSeparateProfilesService _UserSeparateProfilesService ,
                    UserBlockedService _UserBlockedService ,
                    ProfilePicturesService _ProfilePicturesService
            )
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

    @RequestMapping (value = { "" , "/" , "/{id_user}" })
    public AnswerToClient getAll
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @PathVariable (value = "id_user") String id)
    {
        AnswerToClient answerToClient;

        String request = ToJson.CreateClass.n ("id_user" , id).toJson ();
        String router = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ALL_USER;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , SubmitRequestType.get_all_profile_pictures_user);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (!Str.IsEmpty (id))
            {
                ID idUser = new ID (id);
                if (idUser.isValid ())
                {
                    MainAccount mainAccountUser = mainAccountService.findId (idUser.getId ());
                    if (mainAccountUser != null)
                    {
                        UserProfileAccessLevel checkUserAccessLevel = new UserProfileAccessLevel (serviceProfile , mainAccount , mainAccountUser);
                        if (checkUserAccessLevel.hasAccess (Which.cover))
                        {
                            List <ProfilePictures> profilePictures;
                            if (checkUserAccessLevel.isSeparateProfilePictures ())
                                profilePictures = checkUserAccessLevel.getProfilePictures ();
                            else profilePictures = mainAccountUser.getProfilePictures ();

                            if (profilePictures != null && profilePictures.size () > 0)
                            {
                                profilePictures = (new SortProfilePictures (profilePictures)).getNewProfilePictures ();

                                List <Long> ids = new ArrayList <> ();
                                for (ProfilePictures profilePicture : profilePictures)
                                    ids.add (profilePicture.getId ());

                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());
                                answerToClient.put (AnswerToClient.CUK.ids.name () , ids);
                                answerToClient.setReqRes (req , res);
                                l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.found.name ());
                                r.n (mainAccount , SubmitRequestType.get_all_profile_pictures_user , false);
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.not_found.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null);
                                r.n (mainAccount , SubmitRequestType.get_all_profile_pictures_user , true);
                            }
                        }
                        else
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.not_found.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null);
                            r.n (mainAccount , SubmitRequestType.get_all_profile_pictures_user , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.user_not_found.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.user_not_found.name ()) , null);
                        r.n (mainAccount , SubmitRequestType.get_all_profile_pictures_user , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid ();
                    answerToClient.setReqRes (req , res);
                    l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("id invalid") , null);
                    r.n (mainAccount , SubmitRequestType.get_all_profile_pictures_user , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("Request is null") , null);
                r.n (mainAccount , SubmitRequestType.get_all_profile_pictures_user , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }
}
