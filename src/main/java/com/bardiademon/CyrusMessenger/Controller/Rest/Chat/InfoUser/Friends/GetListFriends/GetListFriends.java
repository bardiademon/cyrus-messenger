package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Friends.GetListFriends;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserListService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNFriends.RN_FRIENDS_LIST, method = RequestMethod.POST)
public class GetListFriends
{

    private IsLogin isLogin;

    private final UserLoginService userLoginService;

    private final UserProfileAccessLevel.Service serviceProfile;

    @Autowired
    public GetListFriends
            (MainAccountService _MainAccountService ,
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
        this.serviceProfile = new UserProfileAccessLevel.Service (_MainAccountService ,
                _EnumTypesService ,
                _UserListService ,
                _UserFriendsService ,
                _UserContactsService ,
                _UserSeparateProfilesService ,
                _UserBlockedService ,
                _ProfilePicturesService);

        this.userLoginService = _UserLoginService;
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient getList
            (HttpServletResponse res ,
             @RequestParam ("status") String status ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin , boolean login)
    {
        AnswerToClient answerToClient;

        if (!login)
            isLogin = new IsLogin (codeLogin , userLoginService.Repository);

        if (login || isLogin.isValid ())
        {
            if (status == null || status.isEmpty ()) answerToClient = AnswerToClient.error400 ();
            else
            {
                try
                {
                    answerToClient = AnswerToClient.OK ();
                    List <UserFriends> userFriendsList;
                    if (status.equals (KeyRequest.all.name ()))
                    {
                        answerToClient = AnswerToClient.OK ();
                        answerToClient.put (StatusFriends.deleted.name () ,
                                (getList (res , StatusFriends.deleted.name () , codeLogin , false)).getMessage ());

                        answerToClient.put (StatusFriends.rejected.name () ,
                                (getList (res , StatusFriends.rejected.name () , codeLogin , false)).getMessage ());

                        answerToClient.put (StatusFriends.awaiting_approval.name () ,
                                (getList (res , StatusFriends.awaiting_approval.name () , codeLogin , false)).getMessage ());

                        answerToClient.put (StatusFriends.friend.name () ,
                                (getList (res , StatusFriends.friend.name () , codeLogin , false)).getMessage ());
                    }
                    else
                    {
                        StatusFriends statusFriends = StatusFriends.valueOf (status);
                        MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();
                        userFriendsList = serviceProfile.userFriendsService.Repository.findAllByMainAccountAndStatus
                                (mainAccount , statusFriends);


                        if (userFriendsList.size () > 0)
                        {
                            Map <String, String> friend;
                            UserFriends userFriends;

                            UserProfileAccessLevel checkUserAccessLevel;

                            for (int i = 0; i < userFriendsList.size (); i++)
                            {
                                userFriends = userFriendsList.get (i);
                                friend = new LinkedHashMap <> ();

                                checkUserAccessLevel = new UserProfileAccessLevel (serviceProfile , mainAccount , userFriends.getMainAccountFriend ());
                                if (checkUserAccessLevel.hasAccess (Which.username))
                                    friend.put (KeyAnswer.name.name () , userFriends.getMainAccountFriend ().getUsername ().getUsername ());

                                friend.put (KeyAnswer.status.name () , userFriends.getStatus ().name ());
                                friend.put (KeyAnswer.created_at.name () , Time.toString (userFriends.getCreatedAt ()));
                                friend.put (KeyAnswer.updated_at.name () , Time.toString (userFriends.getUpdatedAt ()));

                                answerToClient.put (String.valueOf (i) , friend);
                            }
                        }
                    }
                }
                catch (IllegalArgumentException e)
                {
                    answerToClient = AnswerToClient.error400 ();
                }
            }

        }
        else answerToClient = isLogin.getAnswerToClient ();

        answerToClient.setResponse (res);
        return answerToClient;
    }

    private enum KeyRequest
    {
        all
    }

    private enum KeyAnswer
    {
        name, status, created_at, updated_at,
    }

}
