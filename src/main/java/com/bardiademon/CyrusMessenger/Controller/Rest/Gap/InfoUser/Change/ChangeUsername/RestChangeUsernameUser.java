package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Change.ChangeUsername;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernameFor;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.Usernames;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = Domain.RNGap.RNInfoUser.RNChange.RN_CHANGE_USERNAME, method = RequestMethod.POST)
public final class RestChangeUsernameUser
{

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;

    @Autowired
    public RestChangeUsernameUser (UserLoginService _UserLoginService , MainAccountService _MainAccountService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient change
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestParam (value = "username") String username)
    {
        AnswerToClient answerToClient;

        String router = Domain.RNGap.RNInfoUser.RNChange.RN_CHANGE_USERNAME;
        SubmitRequestType type = SubmitRequestType.change_username_user;
        String request = ToJson.CreateClass.n ("username" , username).toJson ();

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (!Str.IsEmpty (username))
            {
                VUsername vUsername = new VUsername (username);
                if (vUsername.check ())
                {
                    Usernames oldUsername = mainAccount.getUsername ();
                    if (oldUsername.getUsername ().equals (username))
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.repetitive);
                        answerToClient.setReqRes (req , res);
                        l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.repetitive));
                        r.n (mainAccount , type , true);
                    }
                    else
                    {
                        if (mainAccountService.usernamesService.findForUser (username) == null)
                        {
                            oldUsername.setDeleted (true);
                            oldUsername.setDeletedAt (LocalDateTime.now ());
                            oldUsername.setId2 (mainAccount.getId ());
                            oldUsername.setMainAccount (null);
                            mainAccountService.usernamesService.Repository.save (oldUsername);

                            Usernames newUsername = new Usernames ();
                            newUsername.setMainAccount (mainAccount);
                            newUsername.setUsername (username);
                            newUsername.setUsernameFor (UsernameFor.user);
                            newUsername = mainAccountService.usernamesService.Repository.save (newUsername);

                            mainAccount.setUsername (newUsername);
                            mainAccountService.Repository.save (mainAccount);

                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.changed);
                            answerToClient.setReqRes (req , res);
                            l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.changed);
                            r.n (mainAccount , type , false);
                        }
                        else
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.this_username_used);
                            answerToClient.setReqRes (req , res);
                            l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.this_username_used));
                            r.n (mainAccount , type , true);
                        }
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , AnswerToClient.CUV.username_invalid);
                    answerToClient.setReqRes (req , res);
                    l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.username_invalid));
                    r.n (mainAccount , type , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.request_is_null));
                r.n (mainAccount , type , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum ValAnswer
    {
        this_username_used, repetitive, changed
    }
}

