package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Friends;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import static com.bardiademon.CyrusMessenger.Controller.AnswerToClient.CUK.answer;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
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
import com.bardiademon.CyrusMessenger.bardiademon.CyrusMap;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNGap.RNInfoUser.RNFriends.RN_FRIENDS, method = RequestMethod.POST)
public final class RestFriends
{
    private final String rAdd, rRemove, rApprove, rList;
    private final SubmitRequestType tAdd, tRemove, tApprove, tList;

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

        rAdd = Domain.RNGap.RNInfoUser.RNFriends.RN_FRIENDS_ADD;
        tAdd = SubmitRequestType.add_friend;

        rRemove = Domain.RNGap.RNInfoUser.RNFriends.RN_FRIENDS_DELETE;
        tRemove = SubmitRequestType.del_friend;

        rApprove = Domain.RNGap.RNInfoUser.RNFriends.RN_FRIENDS_APPROVE;
        tApprove = SubmitRequestType.approve_friend;

        rList = Domain.RNGap.RNInfoUser.RNFriends.RN_FRIENDS_LIST;
        tList = SubmitRequestType.list_friends;
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
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.username_belongs_to_you);
                            answerToClient.setReqRes (req , res);
                            l.n (request , rAdd , user , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.username_belongs_to_you) , tAdd , true);
                        }
                        else
                        {
                            UserFriends validFriend = _Service.userFriendsService.findValidFriend (user , friend);
                            if (validFriend == null)
                            {
                                answerToClient = confirmationMethod (user , friend);
                                answerToClient.setReqRes (req , res);
                                l.n (request , rAdd , user , answerToClient , Thread.currentThread ().getStackTrace () , tAdd , false);
                            }
                            else
                            {
                                answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED);
                                answerToClient.put (answer , ValAnswer.is_found);
                                answerToClient.put (KeyAnswer.status , validFriend.getStatus ());
                                answerToClient.setReqRes (req , res);
                                l.n (request , rAdd , user , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.is_found) , tAdd , true);
                            }
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.BadRequest ();
                        answerToClient.put (answer , ValAnswer.username_not_found);
                        answerToClient.setReqRes (req , res);
                        l.n (request , rAdd , user , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.username_not_found) , tAdd , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.BadRequest ();
                    answerToClient.put (answer , ValAnswer.username_not_found);
                    answerToClient.setReqRes (req , res);
                    l.n (request , rAdd , user , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.username_not_found) , tAdd , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.BadRequest ();
                answerToClient.put (answer , ValAnswer.username_invalid);
                answerToClient.setReqRes (req , res);
                l.n (request , rAdd , user , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.username_invalid) , tAdd , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private AnswerToClient confirmationMethod (MainAccount user , MainAccount friend)
    {
        AnswerToClient answerToClient = AnswerToClient.BadRequest ();
        StatusFriends.ApprovalMethod friendConfirmationMethod = friend.getFriendConfirmationMethod ();

        if (friendConfirmationMethod.equals (StatusFriends.ApprovalMethod.reject))
        {
            addFriend (StatusFriends.rejected , user , friend);
            answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED , false);
            answerToClient.put (answer , ValAnswer.friend_request_is_locked);
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
            answerToClient.put (answer , ValAnswer.waiting_for_approval);
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
            (HttpServletRequest req , HttpServletResponse res ,
             @RequestParam ("status") String status ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        String request = ToJson.CreateClass.nj ("status" , status);

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , rList , tList);

        if (both.isOk ())
        {
            if (status == null || status.isEmpty ()) answerToClient = AnswerToClient.BadRequest ();
            else
            {
                assert both.getIsLogin () != null;
                MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

                if (status.equals (Status.requests.name ()))
                {
                    List <String> requests = _Service.userFriendsService.requests (mainAccount.getId ());
                    if (requests != null && requests.size () > 0)
                    {
                        answerToClient = AnswerToClient.KeyAnswer (AnswerToClient.OK () ,
                                AnswerToClient.CUK.answer , AnswerToClient.CUV.found ,
                                KeyAnswer.usernames , requests);
                        answerToClient.setReqRes (req , res);
                        l.n (request , rList , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.found , tList , false);
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found);
                        answerToClient.setReqRes (req , res);
                        l.n (request , rList , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found) , tList , true);
                    }
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
                            CyrusMap <String, String> friend;
                            UserFriends userFriends;

                            UserProfileAccessLevel checkUserAccessLevel;

                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found);
                            for (int i = 0; i < userFriendsList.size (); i++)
                            {
                                userFriends = userFriendsList.get (i);
                                friend = new CyrusMap <> ();

                                checkUserAccessLevel = new UserProfileAccessLevel (mainAccount , userFriends.getMainAccountFriend ());
                                if (checkUserAccessLevel.hasAccess (Which.username))
                                    friend.put (KeyAnswer.name , userFriends.getMainAccountFriend ().getUsername ().getUsername ());

                                friend.put (KeyAnswer.status , userFriends.getStatus ().name ());
                                friend.put (KeyAnswer.created_at , Time.toString (userFriends.getCreatedAt ()));
                                if (userFriends.getUpdatedAt () != null)
                                    friend.put (KeyAnswer.updated_at , Time.toString (userFriends.getUpdatedAt ()));

                                answerToClient.put (String.valueOf (i) , friend);
                            }
                            answerToClient.setReqRes (req , res);
                            l.n (request , rList , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , AnswerToClient.CUV.found , tList , false);

                        }
                        else
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found);
                            answerToClient.setReqRes (req , res);
                            l.n (request , rList , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found) , tList , true);
                        }
                    }
                    catch (IllegalArgumentException e)
                    {
                        answerToClient = AnswerToClient.BadRequest ();
                        answerToClient.setReqRes (req , res);
                        l.n (request , rList , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , e , AnswerToClient.CUV.error , tList , true);
                    }
                }
            }

        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    /**
     * Remove => remove {friend , awaiting_approval => rejected}
     */
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

                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.removed);
                    answerToClient.put (KeyAnswer.status , statusTemp);
                    answerToClient.setReqRes (req , res);
                    l.n (request , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.removed);
                    r.n (mainAccount , tRemove , false);
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.friend_not_found);
                    answerToClient.setReqRes (req , res);
                    l.n (request , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.friend_not_found));
                    r.n (mainAccount , tRemove , true);
                }
            }
            else
            {
                answerToClient = fitd_username.getAnswer ();
                answerToClient.setReqRes (req , res);
                l.n (request , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (FITD_Username.class.getName ()));
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

                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.approved);
                    answerToClient.setReqRes (req , res);
                    l.n (request , rApprove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.approved);
                    r.n (mainAccount , tApprove , false);
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.friend_not_found);
                    answerToClient.setReqRes (req , res);
                    l.n (request , rApprove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.friend_not_found));
                    r.n (mainAccount , tApprove , true);
                }
            }
            else
            {
                answerToClient = fitd_username.getAnswer ();
                answerToClient.setReqRes (req , res);
                l.n (request , rApprove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (FITD_Username.class.getName ()));
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
        friend_not_found, approved, username_belongs_to_you
    }
}
