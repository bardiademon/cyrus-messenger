package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.New.General.NewFriend;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ListUsersForUser.LUFU_Service;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ListUsersForUser.UserFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.FindInTheDatabase.FITD_Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = RouterName.RNNewInfoUser.RNNewFriend.RN_NEW_FRIEND, method = RequestMethod.POST)
public final class NewFriend
{

    private MainAccount user, friend;

    private UserLoginService userLoginService;
    private MainAccountService mainAccountService;
    private UserFriendsService userFriendsService;
    private LUFU_Service lufu_service;

    @Autowired
    public NewFriend
            (UserLoginService _UserLoginService ,
             MainAccountService _MainAccountService ,
             UserFriendsService _UserFriendsService ,
             LUFU_Service LUFU_Service)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.userFriendsService = _UserFriendsService;
        this.lufu_service = LUFU_Service;
    }

    @RequestMapping (value = {"" , ""})
    public AnswerToClient newFriend (HttpServletResponse res , @RequestParam ("username") String username ,
                                     @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        CheckLogin checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
        if (checkLogin.isValid ())
        {
            FITD_Username fitd_username = new FITD_Username (username , mainAccountService);
            if (fitd_username.isValid ())
            {
                if (fitd_username.isFound ())
                {
                    user = checkLogin.getVCodeLogin ().getMainAccount ();
                    friend = fitd_username.getMainAccount ();

                    if (user.getId () == friend.getId ())
                        answerToClient = AnswerToClient.error400 ();
                    else
                    {
                        UserFriends validFriend = userFriendsService.findValidFriend (user , friend);
                        if (validFriend == null)
                            answerToClient = confirmationMethod ();
                        else
                        {
                            answerToClient = AnswerToClient.error400 ();
                            answerToClient.put ("answer" , "is_found");
                            answerToClient.put ("status" , validFriend.getStatus ().name ());
                        }
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.error400 ();
                    answerToClient.put ("answer" , "username_not_found");
                }
            }
            else
            {
                answerToClient = AnswerToClient.error400 ();
                answerToClient.put ("answer" , "username_invalid");
            }
        }
        else answerToClient = checkLogin.getAnswerToClient ();

        answerToClient.setResponse (res);

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
            answerToClient.put ("answer" , "friend_request_is_locked");
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
            answerToClient.put ("answer" , "waiting_for_approval");
        }

        return answerToClient;
    }

    private void addFriend (StatusFriends statusFriends)
    {
        UserFriends userFriends = new UserFriends (user , friend);
        userFriends.setStatus (statusFriends);
        userFriendsService.Repository.save (userFriends);
    }

}
