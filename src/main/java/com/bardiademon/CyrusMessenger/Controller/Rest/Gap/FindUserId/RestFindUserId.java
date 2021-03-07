package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.FindUserId;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNGap.RNInfoUser.RN_FIND_USER_ID, method = RequestMethod.POST)
public final class RestFindUserId
{

    private final UserLoginService userLoginService;
    private final String router;
    private final SubmitRequestType type;

    @Autowired
    public RestFindUserId (final UserLoginService _UserLoginService)
    {
        this.userLoginService = _UserLoginService;

        router = Domain.RNGap.RNInfoUser.RN_MAIN_INFO_USER + "/";
        type = SubmitRequestType.find_user_id;

    }

    @RequestMapping (value = { "/" , "/{username}" })
    public AnswerToClient find
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") final String codeLogin ,
             final HttpServletResponse res , final HttpServletRequest req ,
             @PathVariable (value = "username", required = false) String username)
    {
        final String request = ToJson.CreateClass.nj ("username" , username);
        AnswerToClient answer;

        final CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            final IdUsernameMainAccount account = new IdUsernameMainAccount (This.GetService (MainAccountService.class) , 0 , username);
            if (account.isValid ())
            {
                assert both.getIsLogin () != null;
                final MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
                final UserProfileAccessLevel accessLevel = new UserProfileAccessLevel (mainAccount , account.getMainAccount ());
                accessLevel.setApplicant (mainAccount);
                accessLevel.setUser (account.getMainAccount ());
                if (accessLevel.hasAccess (Which.find_me , Which.username , Which.id))
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.ok);
                    answer.put (AnswerToClient.CUK.id , account.getMainAccount ().getId ());
                    answer.setReqRes (req , res);
                    l.n (request , router , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.ok , type , false);
                }
                else
                {
                    answer = AnswerToClient.AccessDenied ();
                    answer.setReqRes (req , res);
                    l.n (request , router , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.access_denied) , type , true);
                }
            }
            else
                answer = account.getAnswerToClient ();
        }
        else answer = both.getAnswerToClient ();


        return answer;
    }
}
