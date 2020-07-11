package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Friends;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import static com.bardiademon.CyrusMessenger.Controller.AnswerToClient.CUK.answer;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import static com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel._Service;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ListUsersForUser.LUFU_Service;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ListUsersForUser.UserFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNFriends.RN_FRIENDS, method = RequestMethod.POST)
public final class RestFriends
{
    private final String rAdd, rRemove, rApprove;
    private final SubmitRequestType tAdd, tRemove, tApprove;

    private final UserLoginService userLoginService;
    private final UserFriendsService userFriendsService;
    private final UsernamesService usernamesService;
    private final LUFU_Service lufuService;

    @Autowired
    public RestFriends
            (UserLoginService _UserLoginService ,
             UserFriendsService _UserFriendsService ,
             UsernamesService _UsernamesService , LUFU_Service LUFU_Service)
    {
        this.userLoginService = _UserLoginService;
        this.userFriendsService = _UserFriendsService;
        this.usernamesService = _UsernamesService;
        this.lufuService = LUFU_Service;

        rAdd = Domain.RNChat.RNInfoUser.RNFriends.RN_FRIENDS_ADD;
        tAdd = SubmitRequestType.approve_friend;

        rRemove = Domain.RNChat.RNInfoUser.RNFriends.RN_FRIENDS_DELETE;
        tRemove = SubmitRequestType.del_friend;

        rApprove = Domain.RNChat.RNInfoUser.RNFriends.RN_FRIENDS_APPROVE;
        tApprove = SubmitRequestType.approve_friend;
    }

    @RequestMapping (value = "/add")
    public AnswerToClient add
            (HttpServletResponse res , HttpServletRequest req , @RequestParam ("username") String username ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        String request = ToJson.CreateClass.n ("username" , username).toJson ();

        CBSIL both = CBSIL.Both (username , req , res , codeLogin , userLoginService , rAdd , tAdd);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount user = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            FITD_Username fitd_username = new FITD_Username (username , usernamesService);
            if (fitd_username.isValid ())
            {
                if (fitd_username.isFound ())
                {
                    MainAccount friend = fitd_username.getMainAccount ();

                    UserProfileAccessLevel accessLevel = new UserProfileAccessLevel (user , friend);
                    if (accessLevel.hasAccess (Which.username))
                    {
                        if (user.getId () == friend.getId ())
                            answerToClient = AnswerToClient.error400 ();
                        else
                        {
                            UserFriends validFriend = _Service.userFriendsService.findValidFriend (user , friend);
                            if (validFriend == null)
                            {
                                answerToClient = confirmationMethod (user , friend);

                                answerToClient.setReqRes (req , res);
                                l.n (request , rAdd , user , answerToClient , Thread.currentThread ().getStackTrace () , null , null);
                                r.n (user , tAdd , false);
                            }
                            else
                            {
                                answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED);
                                answerToClient.put (answer.name () , ValAnswer.is_found.name ());
                                answerToClient.put (KeyAnswer.status.name () , validFriend.getStatus ().name ());
                                answerToClient.setReqRes (req , res);
                                l.n (request , rAdd , user , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.is_found.name ()) , null);
                                r.n (user , tAdd , true);
                            }
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.error400 ();
                        answerToClient.put (answer.name () , ValAnswer.username_not_found.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (request , rAdd , user , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_not_found.name ()) , null);
                        r.n (user , tAdd , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.error400 ();
                    answerToClient.put (answer.name () , ValAnswer.username_not_found.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (request , rAdd , user , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_not_found.name ()) , null);
                    r.n (user , tAdd , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.error400 ();
                answerToClient.put (answer.name () , ValAnswer.username_invalid.name ());
                answerToClient.setReqRes (req , res);
                l.n (request , rAdd , user , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_invalid.name ()) , null);
                r.n (user , tAdd , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private AnswerToClient confirmationMethod (MainAccount user , MainAccount friend)
    {
        AnswerToClient answerToClient = AnswerToClient.error400 ();
        StatusFriends.ApprovalMethod friendConfirmationMethod = friend.getFriendConfirmationMethod ();

        if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.reject))
        {
            addFriend (StatusFriends.rejected , user , friend);
            answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED , false);
            answerToClient.put (answer.name () , ValAnswer.friend_request_is_locked.name ());
        }
        else if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.approve_all))
        {
            addFriend (StatusFriends.friend , user , friend);
            answerToClient = AnswerToClient.OK ();
        }
        else if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.just_list))
        {
            if (lufuService.findValidUser (user , friend , UserFor.confirm_friends) != null)
            {
                addFriend (StatusFriends.friend , user , friend);
                answerToClient = AnswerToClient.OK ();
            }
            else
                answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED , false);
        }
        else if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.all_except))
        {
            if (lufuService.findValidUser (user , friend , UserFor.except) == null)
            {
                addFriend (StatusFriends.friend , user , friend);
                answerToClient = AnswerToClient.OK ();
            }
            else
                answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED , false);
        }
        else if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.just_list_family))
        {
            if (lufuService.findValidUser (user , friend , UserFor.is_family) != null)
            {
                addFriend (StatusFriends.friend , user , friend);
                answerToClient = AnswerToClient.OK ();
            }
            else
                answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED , false);
        }
        else if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.wait))
        {
            addFriend (StatusFriends.awaiting_approval , user , friend);
            answerToClient = AnswerToClient.OK ();
            answerToClient.put (answer.name () , ValAnswer.waiting_for_approval.name ());
        }

        return answerToClient;
    }

    private void addFriend (StatusFriends statusFriends , MainAccount user , MainAccount friend)
    {
        UserFriends userFriends = new UserFriends (user , friend);
        userFriends.setStatus (statusFriends);
        _Service.userFriendsService.Repository.save (userFriends);
    }

    @RequestMapping (value = { "/list" })
    public AnswerToClient list
            (HttpServletResponse res ,
             @RequestParam ("status") String status ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);

        if (isLogin.isValid ())
        {
            if (status == null || status.isEmpty ()) answerToClient = AnswerToClient.error400 ();
            else
            {
                MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();

                answerToClient = AnswerToClient.OK ();

                if (status.equals (Status.requests.name ()))
                {
                    List <String> requests = _Service.userFriendsService.requests (mainAccount.getId ());
                    if (requests != null && requests.size () > 0)
                        AnswerToClient.OneAnswer (answerToClient , KeyAnswer.usernames.name () , requests);
                    else
                        AnswerToClient.OneAnswer (answerToClient , AnswerToClient.CUV.not_found.name ());
                }
                else
                {
                    try
                    {
                        List <UserFriends> userFriendsList;
                        StatusFriends statusFriends = StatusFriends.valueOf (status);
                        userFriendsList = _Service.userFriendsService.Repository.findAllByMainAccountAndStatus
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

                                checkUserAccessLevel = new UserProfileAccessLevel (mainAccount , userFriends.getMainAccountFriend ());
                                if (checkUserAccessLevel.hasAccess (Which.username))
                                    friend.put (KeyAnswer.name.name () , userFriends.getMainAccountFriend ().getUsername ().getUsername ());

                                friend.put (KeyAnswer.status.name () , userFriends.getStatus ().name ());
                                friend.put (KeyAnswer.created_at.name () , Time.toString (userFriends.getCreatedAt ()));
                                friend.put (KeyAnswer.updated_at.name () , Time.toString (userFriends.getUpdatedAt ()));

                                answerToClient.put (String.valueOf (i) , friend);
                            }
                        }

                    }
                    catch (IllegalArgumentException e)
                    {
                        answerToClient = AnswerToClient.error400 ();
                    }
                }
            }

        }
        else answerToClient = isLogin.getAnswerToClient ();

        answerToClient.setResponse (res);
        return answerToClient;
    }

    @RequestMapping (value = { "/remove" }, method = RequestMethod.POST)
    public AnswerToClient remove
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestParam (value = "username") String username)
    {
        AnswerToClient answerToClient;
        String request = ToJson.CreateClass.n ("username" , username).toJson ();

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , rRemove , tRemove);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            FITD_Username fitd_username = new FITD_Username (username , usernamesService);
            if (fitd_username.isFound ())
            {
                UserFriends friend = userFriendsService.findValidFriend (mainAccount , fitd_username.getMainAccount ());
                if (friend != null)
                {
                    StatusFriends statusTemp = friend.getStatus ();

                    friend.setDeleted (true);
                    friend.setDeletedAt (LocalDateTime.now ());

                    if (statusTemp.equals (StatusFriends.awaiting_approval))
                    {
                        friend.setStatus (StatusFriends.rejected);
                        statusTemp = StatusFriends.rejected;
                    }
                    else
                        friend.setStatus (StatusFriends.deleted);

                    userFriendsService.Repository.save (friend);

                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.removed.name ());
                    answerToClient.put (KeyAnswer.status.name () , statusTemp);
                    answerToClient.setReqRes (req , res);
                    l.n (request , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.removed.name ());
                    r.n (mainAccount , tRemove , false);
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.friend_not_found.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (request , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.friend_not_found.name ()) , null);
                    r.n (mainAccount , tRemove , true);
                }
            }
            else
            {
                answerToClient = fitd_username.getAnswer ();
                answerToClient.setReqRes (req , res);
                l.n (request , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (FITD_Username.class.getName ()) , null);
                r.n (mainAccount , tRemove , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    @RequestMapping (value = { "/approve" }, method = RequestMethod.POST)
    public AnswerToClient approve
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestParam (value = "username") String username)
    {
        AnswerToClient answerToClient;
        String request = ToJson.CreateClass.n ("username" , username).toJson ();

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , rApprove , tApprove);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            FITD_Username fitd_username = new FITD_Username (username , usernamesService);
            if (fitd_username.isFound ())
            {
                UserFriends friend = userFriendsService.findFriend (fitd_username.getMainAccount () , mainAccount , StatusFriends.awaiting_approval);
                if (friend != null)
                {
                    friend.setStatus (StatusFriends.friend);
                    userFriendsService.Repository.save (friend);

                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.approved.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (request , rApprove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.approved.name ());
                    r.n (mainAccount , tApprove , false);
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.friend_not_found.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (request , rApprove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.friend_not_found.name ()) , null);
                    r.n (mainAccount , tApprove , true);
                }
            }
            else
            {
                answerToClient = fitd_username.getAnswer ();
                answerToClient.setReqRes (req , res);
                l.n (request , rApprove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (FITD_Username.class.getName ()) , null);
                r.n (mainAccount , tApprove , true);
            }
        }
        else
            answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum Status
    {
        requests
    }

    private enum KeyAnswer
    {
        name, status, created_at, updated_at, usernames
    }

    public enum ValAnswer
    {
        waiting_for_approval, friend_request_is_locked, username_invalid, username_not_found, is_found,
        friend_not_found, approved
    }
}
