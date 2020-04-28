package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Friends.NewFriend;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import static com.bardiademon.CyrusMessenger.Controller.AnswerToClient.CUK.answer;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ListUsersForUser.LUFU_Service;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ListUsersForUser.UserFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserListService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNFriends.RN_FRIENDS_ADD, method = RequestMethod.POST)
public final class NewFriend
{
    private final String router;
    private final SubmitRequestType type;

    private MainAccount user, friend;

    private UserLoginService userLoginService;
    private UserFriendsService userFriendsService;
    private UsernamesService usernamesService;
    private LUFU_Service lufu_service;

    private final UserProfileAccessLevel.Service service;

    @Autowired
    public NewFriend
            (UserLoginService _UserLoginService ,
             UserFriendsService _UserFriendsService ,
             UsernamesService _UsernamesService ,
             LUFU_Service LUFU_Service ,
             MainAccountService _MainAccountService ,
             EnumTypesService _EnumTypesService ,
             UserListService _UserListService ,
             UserContactsService _UserContactsService ,
             UserSeparateProfilesService _UserSeparateProfilesService ,
             UserBlockedService _UserBlockedService ,
             ProfilePicturesService _ProfilePicturesService)
    {
        this.service = new UserProfileAccessLevel.Service
                (_MainAccountService , _EnumTypesService , _UserListService , _UserFriendsService , _UserContactsService ,
                        _UserSeparateProfilesService , _UserBlockedService , _ProfilePicturesService);
        this.router = Domain.RNChat.RNInfoUser.RNFriends.RN_FRIENDS_ADD;
        this.type = SubmitRequestType.add_friend;
        this.userLoginService = _UserLoginService;
        this.userFriendsService = _UserFriendsService;
        this.usernamesService = _UsernamesService;
        this.lufu_service = LUFU_Service;
    }

    @RequestMapping (value = { "" , "" })
    public AnswerToClient newFriend
            (HttpServletResponse res , HttpServletRequest req , @RequestParam ("username") String username ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        String request = ToJson.CreateClass.n ("username" , username).toJson ();

        CBSIL both = CBSIL.Both (username , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            FITD_Username fitd_username = new FITD_Username (username , usernamesService);
            if (fitd_username.isValid ())
            {
                if (fitd_username.isFound ())
                {
                    assert both.getIsLogin () != null;
                    user = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
                    friend = fitd_username.getMainAccount ();

                    UserProfileAccessLevel accessLevel = new UserProfileAccessLevel (service , user , friend);
                    if (accessLevel.hasAccess (Which.username))
                    {
                        if (user.getId () == friend.getId ())
                            answerToClient = AnswerToClient.error400 ();
                        else
                        {
                            UserFriends validFriend = userFriendsService.findValidFriend (user , friend);
                            if (validFriend == null)
                            {
                                answerToClient = confirmationMethod ();

                                answerToClient.setReqRes (req , res);
                                l.n (request , router , user , answerToClient , Thread.currentThread ().getStackTrace () , null , null);
                                r.n (user , type , false);
                            }
                            else
                            {
                                answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED);
                                answerToClient.put (answer.name () , ValAnswer.is_found.name ());
                                answerToClient.put (KeyAnswer.status.name () , validFriend.getStatus ().name ());
                                answerToClient.setReqRes (req , res);
                                l.n (request , router , user , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.is_found.name ()) , null);
                                r.n (user , type , true);
                            }
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.error400 ();
                        answerToClient.put (answer.name () , ValAnswer.username_not_found.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (request , router , user , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_not_found.name ()) , null);
                        r.n (user , type , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.error400 ();
                    answerToClient.put (answer.name () , ValAnswer.username_not_found.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (request , router , user , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_not_found.name ()) , null);
                    r.n (user , type , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.error400 ();
                answerToClient.put (answer.name () , ValAnswer.username_invalid.name ());
                answerToClient.setReqRes (req , res);
                l.n (request , router , user , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_invalid.name ()) , null);
                r.n (user , type , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private AnswerToClient confirmationMethod ()
    {
        AnswerToClient answerToClient = AnswerToClient.error400 ();
        StatusFriends.ApprovalMethod friendConfirmationMethod = friend.getFriendConfirmationMethod ();

        if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.reject))
        {
            addFriend (StatusFriends.rejected);
            answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED , false);
            answerToClient.put (answer.name () , ValAnswer.friend_request_is_locked.name ());
        }
        else if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.approve_all))
        {
            addFriend (StatusFriends.friend);
            answerToClient = AnswerToClient.OK ();
        }
        else if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.just_list))
        {
            if (lufu_service.findValidUser (user , friend , UserFor.confirm_friends) != null)
            {
                addFriend (StatusFriends.friend);
                answerToClient = AnswerToClient.OK ();
            }
            else
                answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED , false);
        }
        else if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.all_except))
        {
            if (lufu_service.findValidUser (user , friend , UserFor.except) == null)
            {
                addFriend (StatusFriends.friend);
                answerToClient = AnswerToClient.OK ();
            }
            else
                answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED , false);
        }
        else if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.just_list_family))
        {
            if (lufu_service.findValidUser (user , friend , UserFor.is_family) != null)
            {
                addFriend (StatusFriends.friend);
                answerToClient = AnswerToClient.OK ();
            }
            else
                answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED , false);
        }
        else if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.wait))
        {
            addFriend (StatusFriends.awaiting_approval);
            answerToClient = AnswerToClient.OK ();
            answerToClient.put (answer.name () , ValAnswer.waiting_for_approval.name ());
        }

        return answerToClient;
    }

    private void addFriend (StatusFriends statusFriends)
    {
        UserFriends userFriends = new UserFriends (user , friend);
        userFriends.setStatus (statusFriends);
        userFriendsService.Repository.save (userFriends);
    }

    public enum KeyAnswer
    {
        status
    }

    public enum ValAnswer
    {
        waiting_for_approval, friend_request_is_locked, username_invalid, username_not_found, is_found
    }

}
