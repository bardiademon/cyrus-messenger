package com.bardiademon.CyrusMessenger.Controller.Rest.RestRegister;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed.Confirmed;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed.ConfirmedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNRegister.RN_MAIN, method = RequestMethod.POST)
public final class RestRegister
{

    private final MainAccountService mainAccountService;
    private final ConfirmedService confirmedService;
    private final ConfirmCodeService confirmCodeService;
    private final UsernamesService usernamesService;
    private final UserLoginService userLoginService;

    private final SubmitRequestType type;
    private final String router;

    @Autowired
    public RestRegister
            (MainAccountService _MainAccountService ,
             ConfirmedService _ConfirmedService ,
             ConfirmCodeService _ConfirmCodeService ,
             UsernamesService _UsernamesService ,
             UserLoginService _UserLoginService)
    {
        this.mainAccountService = _MainAccountService;
        this.confirmedService = _ConfirmedService;
        this.confirmCodeService = _ConfirmCodeService;
        this.usernamesService = _UsernamesService;
        this.userLoginService = _UserLoginService;

        this.type = SubmitRequestType.register;
        this.router = Domain.RNRegister.RN_MAIN;
    }

    @RequestMapping ({ "/" , "" })
    public AnswerToClient register
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletRequest req , HttpServletResponse res ,
             @RequestBody RegisterRequest request)
    {
        AnswerToClient answerToClient = null;

        String strReq = ToJson.To (request);

        CBSIL cbsil = CBSIL.BSubmitRequest (strReq , router , req , res , type , codeLogin , userLoginService);
        if (cbsil.isOk ())
        {
            MainAccount mainAccount = null;
            if (cbsil.getIsLogin () != null) mainAccount = cbsil.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null && !isNull (request))
            {
                if (Str.IsEmpty (request.getCodeConfirmedPhone ()))
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.code_confirmed_phone_is_empty.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (strReq , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.code_confirmed_phone_is_empty.name ()) , null);
                    r.nim (req.getRemoteAddr () , mainAccount , type , true);
                }
                else
                {
                    Confirmed confirmed = confirmedService.getConfirmedIsActiveConfirmed (request.getCodeConfirmedPhone ());
                    if (confirmed == null)
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.code_confirmed_phone_invalid.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (strReq , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.code_confirmed_phone_invalid.name ()) , null);
                        r.nim (req.getRemoteAddr () , mainAccount , type , true);
                    }
                    else
                    {
                        boolean okEmail = true;

                        Confirmed confirmedEmail = null;

                        if (request.getCodeEmail () != null && !request.getCodeEmail ().equals (request.EMAIL_NULL))
                        {
                            confirmedEmail = confirmedService.hasCodeFor (request.getCodeConfirmedPhone () , ConfirmCodeFor.email);
                            if (confirmedEmail == null)
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.code_confirmed_email_invalid.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (strReq , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.code_confirmed_email_invalid.name ()) , null);
                                r.nim (req.getRemoteAddr () , mainAccount , type , true);
                                okEmail = false;
                            }
                        }

                        if (okEmail)
                        {
                            FITD_Username fitd_username = new FITD_Username (request.getUsername () , usernamesService);
                            if (!fitd_username.isValid ())
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.username_invalid.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (strReq , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_invalid.name ()) , null);
                                r.nim (req.getRemoteAddr () , mainAccount , type , true);
                            }
                            else
                            {
                                if (fitd_username.isFound ())
                                {
                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.username_used.name ());
                                    answerToClient.setReqRes (req , res);
                                    l.n (strReq , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_used.name ()) , null);
                                    r.nim (req.getRemoteAddr () , mainAccount , type , true);
                                }
                                else
                                {
                                    MainAccount mainAccountCP = confirmed.getConfirmCode ().getMainAccount ();
                                    if (mainAccountCP == null || mainAccountCP.isDeleted () || (mainAccountService.findPhone (confirmed.getValue ())) == null)
                                    {
                                        boolean addedAccount = mainAccountService.newAccount (request , confirmed , confirmedEmail , confirmCodeService);
                                        if (addedAccount)
                                        {
                                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.recorded.name ());
                                            answerToClient.put (KeyAnswer.phone.name () , confirmed.getValue ());
                                            if (confirmedEmail != null)
                                                answerToClient.put (KeyAnswer.email.name () , confirmedEmail.getValue ());
                                            answerToClient.setReqRes (req , res);
                                            l.n (strReq , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.recorded.name ());
                                            r.nim (req.getRemoteAddr () , mainAccount , type , false);
                                        }
                                        else
                                        {
                                            answerToClient = AnswerToClient.ServerError ();
                                            answerToClient.setReqRes (req , res);
                                            l.n (strReq , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , null);
                                            r.nim (req.getRemoteAddr () , mainAccount , type , true);
                                        }
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.phone_is_exists.name ());
                                        answerToClient.setReqRes (req , res);
                                        l.n (strReq , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.phone_is_exists.name ()) , null);
                                        r.nim (req.getRemoteAddr () , mainAccount , type , true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (strReq , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null);
                r.nim (req.getRemoteAddr () , mainAccount , type , true);
            }
        }
        else answerToClient = cbsil.getAnswerToClient ();

        return answerToClient;
    }

    private boolean isNull (RegisterRequest registerRequest)
    {
        return (Str.IsEmpty (registerRequest.getFamily ()) || Str.IsEmpty (registerRequest.getName ())
                || Str.IsEmpty (registerRequest.getUsername ()));
    }

    private enum KeyAnswer
    {
        phone, email
    }

    private enum ValAnswer
    {
        recorded, request_is_null, phone, username, code_confirmed_phone_invalid, code_confirmed_email_invalid, code_confirmed_phone_is_empty,
        password, name, family, phone_is_exists, username_invalid, username_used
    }

}