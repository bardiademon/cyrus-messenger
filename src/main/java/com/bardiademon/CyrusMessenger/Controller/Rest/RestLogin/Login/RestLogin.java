package com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP.IsValidUEPRequest;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP.RestIsValidUEP;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedFor;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails.EmailFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails.UserEmails;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import com.bardiademon.CyrusMessenger.bardiademon.Hash256;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNLogin.RN_LOGIN, method = RequestMethod.POST)
public class RestLogin
{

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;
    private final SubmitRequestService submitRequestService;
    private final UsernamesService usernamesService;

    @Autowired
    public RestLogin
            (UserLoginService _UserLoginService ,
             MainAccountService _MainAccountService ,
             SubmitRequestService _SubmitRequestService ,
             UsernamesService _UsernamesService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.submitRequestService = _SubmitRequestService;
        this.usernamesService = _UsernamesService;
    }

    @RequestMapping ({ "/" , "" })
    public AnswerToClient login
            (@RequestBody LoginRequest request ,
             HttpServletRequest req , HttpServletResponse res , @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;
        CheckBlockSystem checkBlockSystem;
        if ((checkBlockSystem = new CheckBlockSystem (req , submitRequestService.blockedByTheSystemService , BlockedFor.submit_request , SubmitRequestType.login.name ())).isBlocked ())
        {
            answerToClient = checkBlockSystem.getAnswerToClient ();

            answerToClient.setReqRes (req , res);

            ToJson.CreateClass createClass = new ToJson.CreateClass ();
            createClass.put ("blocked_for" , BlockedFor.submit_request).put ("submit_request_type" , SubmitRequestType.login);
            l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , null , answerToClient , Thread.currentThread ().getStackTrace () , l.e ( "is_block") , createClass.toJson ());
        }
        else
        {
            RestIsValidUEP restIsValidUEP = new RestIsValidUEP (mainAccountService , submitRequestService , usernamesService);
            answerToClient = restIsValidUEP.isValid (request.getIsValidUEPRequest () , res , req , true);

            Map <String, Object> message = answerToClient.getMessage ();

            Object objValid;
            boolean valid = (boolean) ((objValid = message.get (RestIsValidUEP.KeyAnswer.is_valid.name ())) == null ? false : objValid);

            boolean isOk = answerToClient.isOk ();
            boolean is200 = answerToClient.getStatusCode () == 200;
            if (isOk && is200 && valid)
            {
                IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
                if (isLogin.isValid ())
                {
                    l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , isLogin.getVCodeLogin ().getMainAccount () , null , Thread.currentThread ().getStackTrace () , null , "is login");
                    userLoginService.logout (codeLogin);
                }

                MainAccount mainAccount;
                if ((request.getPassword () != null && !request.getPassword ().equals ("")) && (mainAccount = checkPassword (request.getValueUEP () , request.getUep () , request.getPassword ())) != null)
                {
                    int counterCreateCode = 0;

                    String code = null;
                    boolean createCode = false;
                    int MAX_CREATE_CODE = 5;
                    while ((counterCreateCode++) <= MAX_CREATE_CODE)
                    {
                        code = Code.CreateCodeLogin ();
                        if (userLoginService.Repository.findByCodeLogin (code) == null)
                        {
                            createCode = true;
                            break;
                        }
                    }
                    if (createCode)
                    {
                        assert code != null;

                        UserLoginService.ResNewLogin resNewLogin = userLoginService.newLogin (code , mainAccount , req.getRemoteAddr ());
                        if (resNewLogin.isLogin ())
                        {
                            answerToClient = AnswerToClient.OK ();
                            answerToClient.put (KeyAnswer.is_login , true);
                            answerToClient.put (KeyAnswer.code_login , code);
                            answerToClient.put (KeyAnswer.credit_up , Time.toString (resNewLogin.getCreditUp ()));
                            res.addCookie (MCookie.CookieApi (code));
                            submitRequestService.newRequest (req.getRemoteAddr () , SubmitRequestType.login , false);

                            l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , "login");
                        }
                        else
                        {
                            userLoginService.loginFailed (req.getRemoteAddr ());
                            answerToClient = AnswerToClient.ServerError ();
                            answerToClient.setReqRes (req , res);

                            ToJson.CreateClass createClass = new ToJson.CreateClass ();
                            createClass.put ("code" , code).put ("account" , ToJson.To (mainAccount)).put ("ip" , req.getRemoteAddr ());
                            l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e ( "can not save new login") , createClass.toJson ());
                        }
                        System.gc ();
                    }
                    else
                    {
                        answerToClient = AnswerToClient.ServerError ();
                        ToJson.CreateClass createClass = new ToJson.CreateClass ();
                        createClass.put ("code" , code).put ("account" , ToJson.To (mainAccount)).put ("ip" , req.getRemoteAddr ());
                        l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e ( "can not create code") , createClass.toJson ());
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , KeyAnswer.password_is_valid , false);

                    answerToClient.setReqRes (req , res);

                    l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , null , answerToClient , Thread.currentThread ().getStackTrace () , l.e ( KeyAnswer.password_is_valid) , KeyAnswer.password_is_valid);
                    submitRequestService.newRequest (req.getRemoteAddr () , SubmitRequestType.login , true);
                }
            }
            else
            {
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , null , answerToClient , Thread.currentThread ().getStackTrace () , l.e ("not valid info"));
            }
        }
        answerToClient.setReqRes (req , res);
        System.gc ();
        return answerToClient;
    }


    private MainAccount checkPassword (String uep , String valueEup , String password)
    {
        if (password == null || password.equals ("")) return null;

        password = (new Hash256 ()).hash (password);

        MainAccount mainAccount = null;
        switch (uep)
        {
            case IsValidUEPRequest.PHONE:
                mainAccount = mainAccountService.findPhone (valueEup , password);
                break;
            case IsValidUEPRequest.EMAIL:
                UserEmails userEmails = mainAccountService.userEmailsService.find (valueEup , EmailFor.ma);
                if (userEmails != null)
                {
                    if (userEmails.getMainAccount ().getPassword ().equals (password))
                        mainAccount = userEmails.getMainAccount ();
                }
                break;
            case IsValidUEPRequest.USERNAME:
                FITD_Username fitd_username = new FITD_Username (valueEup , usernamesService);
                if (fitd_username.isFound ())
                {
                    if (fitd_username.getMainAccount ().getPassword ().equals (password))
                        mainAccount = fitd_username.getMainAccount ();
                }
                break;
        }
        return mainAccount;
    }

    public enum KeyAnswer
    {
        password_is_valid, is_login, code_login, credit_up
    }
}
