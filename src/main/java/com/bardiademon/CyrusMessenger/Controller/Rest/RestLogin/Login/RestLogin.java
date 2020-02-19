package com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP.IsValidUEPRequest;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP.RestIsValidUEP;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedFor;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UsersStatus.UsersStatusService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.bardiademon.CyrusMessenger.bardiademon.Hash256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping (value = Domain.RNLogin.RN_LOGIN, method = RequestMethod.POST)
public class RestLogin
{

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;
    private UsersStatusService usersStatusService;
    private SubmitRequestService submitRequestService;

    @Autowired
    public RestLogin
            (UserLoginService _UserLoginService ,
             MainAccountService _MainAccountService ,
             UsersStatusService _UsersStatusService ,
             SubmitRequestService _SubmitRequestService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.usersStatusService = _UsersStatusService;
        this.submitRequestService = _SubmitRequestService;
    }

    @RequestMapping ({"/" , ""})
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
            createClass.put ("blocked_for" , BlockedFor.submit_request.name ()).put ("submit_request_type" , SubmitRequestType.login.name ());
            l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("is_block") , createClass.toJson ());
        }
        else
        {
            RestIsValidUEP restIsValidUEP = new RestIsValidUEP (mainAccountService , usersStatusService , submitRequestService);
            answerToClient = restIsValidUEP.isValid (request.getIsValidUEPRequest () , res , req , true);

            Map<String, Object> message = answerToClient.getMessage ();

            Object objValid;
            boolean valid = (boolean) ((objValid = message.get (RestIsValidUEP.KeyAnswer.is_valid.name ())) == null ? false : objValid);

            boolean isOk = answerToClient.isOk ();
            boolean is200 = answerToClient.getStatusCode () == 200;
            if (isOk && is200 && valid)
            {
                CheckLogin checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
                if (checkLogin.isValid ())
                {
                    l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , checkLogin.getVCodeLogin ().getMainAccount () , null , Thread.currentThread ().getStackTrace () , null , "is login");
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
                        if (userLoginService.newLogin (code , mainAccount , req.getRemoteAddr ()))
                        {
                            answerToClient = new AnswerToClient (200 , true);
                            answerToClient.put (KeyAnswer.is_login.name () , true);
                            answerToClient.put (KeyAnswer.code_login.name () , code);
                            answerToClient.put (KeyAnswer.credit_up.name () , userLoginService.getCreditUp ());
                            res.addCookie (MCookie.CookieApi (code));
                            submitRequestService.newRequest (req.getRemoteAddr () , SubmitRequestType.login , false);

                            l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , "login");
                        }
                        else
                        {
                            answerToClient = AnswerToClient.ServerError ();
                            answerToClient.setReqRes (req , res);

                            ToJson.CreateClass createClass = new ToJson.CreateClass ();
                            createClass.put ("code" , code).put ("account" , ToJson.To (mainAccount)).put ("ip" , req.getRemoteAddr ());
                            l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("can not save new login") , createClass.toJson ());
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.ServerError ();
                        ToJson.CreateClass createClass = new ToJson.CreateClass ();
                        createClass.put ("code" , code).put ("account" , ToJson.To (mainAccount)).put ("ip" , req.getRemoteAddr ());
                        l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("can not create code") , createClass.toJson ());
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , KeyAnswer.password_is_valid.name () , false);

                    answerToClient.setReqRes (req , res);

                    l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (KeyAnswer.password_is_valid.name ()) , KeyAnswer.password_is_valid.name ());
                    submitRequestService.newRequest (req.getRemoteAddr () , SubmitRequestType.login , true);
                }
            }
            else
            {
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , Domain.RNLogin.RN_LOGIN , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("not valid info") , null);
            }
        }
        answerToClient.setReqRes (req , res);
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
                mainAccount = mainAccountService.findEmail (valueEup , password);
                break;
            case IsValidUEPRequest.USERNAME:
                mainAccount = mainAccountService.findUsername (valueEup , password);
                break;
        }
        return mainAccount;
    }

    public enum KeyAnswer
    {
        password_is_valid, is_login, code_login, credit_up
    }
}
