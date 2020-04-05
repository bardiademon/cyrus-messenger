package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.New.General.NewFriend;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ListUsersForUser.LUFU_Service;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ListUsersForUser.UserFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.HttpServletResponse;

import static com.bardiademon.CyrusMessenger.Controller.AnswerToClient.CUK.answer;

@RestController
@RequestMapping (value = Domain.RNChat.RNNewInfoUser.RN_NEW_FRIEND, method = RequestMethod.POST)
public final class NewFriend
{

    private MainAccount user, friend;

    private UserLoginService userLoginService;
    private UserFriendsService userFriendsService;
    private UsernamesService usernamesService;
    private LUFU_Service lufu_service;

    @Autowired
    public NewFriend
            (UserLoginService _UserLoginService ,
             UserFriendsService _UserFriendsService ,
             UsernamesService _UsernamesService,
             LUFU_Service LUFU_Service)
    {
        this.userLoginService = _UserLoginService;
        this.userFriendsService = _UserFriendsService;
        this.usernamesService = _UsernamesService;
        this.lufu_service = LUFU_Service;
    }

    @RequestMapping (value = {"" , ""})
    public AnswerToClient newFriend (HttpServletResponse res , @RequestParam ("username") String username ,
                                     @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
        {
            FITD_Username fitd_username = new FITD_Username (username , usernamesService);
            if (fitd_username.isValid ())
            {
                if (fitd_username.isFound ())
                {
                    user = isLogin.getVCodeLogin ().getMainAccount ();
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
                            answerToClient.put (answer.name () , ValAnswer.is_found.name ());
                            answerToClient.put (KeyAnswer.status.name () , validFriend.getStatus ().name ());
                        }
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.error400 ();
                    answerToClient.put (answer.name () , ValAnswer.username_not_found.name ());
                }
            }
            else
            {
                answerToClient = AnswerToClient.error400 ();
                answerToClient.put (answer.name () , ValAnswer.username_invalid.name ());
            }
        }
        else answerToClient = isLogin.getAnswerToClient ();

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
