package com.bardiademon.CyrusMessenger.Controller.Rest.RestConfirm.ConfirmEmail;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VEmail;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultKey;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCode;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed.Confirmed;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed.ConfirmedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails.UserEmailsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SendMail.SendConformCode;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNConfirm.RN_CONFIRM_EMAIL, method = RequestMethod.POST)
public final class ConfirmEmail
{

    private final UserLoginService userLoginService;
    private final UserEmailsService userEmailsService;
    private final ConfirmedService confirmedService;
    private final ConfirmCodeService confirmCodeService;
    private final DefaultService defaultService;

    /**
     * SC => Send Code
     */
    private final String routerSC;
    private final SubmitRequestType typeSC;

    /**
     * C => Confirm
     */
    private final String routerC;
    private final SubmitRequestType typeC;

    @Autowired
    public ConfirmEmail
            (UserLoginService _UserLoginService ,
             UserEmailsService _UserEmailsService ,
             ConfirmedService _ConfirmedService ,
             ConfirmCodeService _ConfirmCodeService ,
             DefaultService _DefaultService)
    {
        this.userLoginService = _UserLoginService;
        this.userEmailsService = _UserEmailsService;
        this.confirmedService = _ConfirmedService;
        this.confirmCodeService = _ConfirmCodeService;
        this.defaultService = _DefaultService;

        this.routerSC = Domain.RNConfirm.RN_CONFIRM_EMAIL;
        this.typeSC = SubmitRequestType.check_and_send_code__conform_email;

        this.routerC = Domain.RNConfirm.RN_CONFIRM_EMAIL + "/confirm";
        this.typeC = SubmitRequestType.check_and_send_code__conform_email;
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient checkAndSendCode
            (HttpServletResponse res , HttpServletRequest req ,
             @RequestParam ("email") String email , @CookieValue (name = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        final String request = ToJson.CreateClass.nj ("email" , email);

        final CBSIL cbsil = CBSIL.BSubmitRequest (request , routerSC , req , res , typeSC , codeLogin , userLoginService);

        if (cbsil.isOk ())
        {
            MainAccount mainAccount = null;
            if (cbsil.getIsLogin () != null) mainAccount = cbsil.getIsLogin ().getVCodeLogin ().getMainAccount ();

            if (!Str.IsEmpty (email))
            {
                final VEmail vEmail = new VEmail (email);
                if (vEmail.check ())
                {
                    if (!userEmailsService.find ())
                    {
                        final Integer min = defaultService.getInt (DefaultKey.ce_min_valid);
                        if (min != null)
                        {
                            ConfirmCode sendCode = confirmCodeService.Repository.findCode (LocalDateTime.now () , LocalDateTime.now ().plusMinutes (min));
                            if (sendCode == null)
                            {
                                List <ConfirmCode> lstFindCode
                                        = confirmCodeService.Repository.findCode (ConfirmCodeFor.email , email , LocalDateTime.now ());

                                if (lstFindCode != null && lstFindCode.size () > 0)
                                {
                                    for (int i = 0; i < lstFindCode.size (); i++)
                                    {
                                        ConfirmCode confirmCode = lstFindCode.get (i);
                                        confirmCode.setDeleted (true);
                                        confirmCode.setDeletedAt (LocalDateTime.now ());
                                        lstFindCode.set (i , confirmCode);
                                    }
                                    confirmCodeService.Repository.saveAll (lstFindCode);
                                    answerToClient = AnswerToClient.ServerError ();
                                    answerToClient.setReqRes (req , res);
                                    l.n (request , routerSC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , null);
                                    r.nim (req.getRemoteAddr () , mainAccount , typeSC , true);
                                }
                                else
                                {
                                    final Object wait = new Object ();
                                    AtomicReference <String> code = new AtomicReference <> ();
                                    AtomicBoolean createCode = new AtomicBoolean (false);
                                    new Thread (() -> Code.CreateCodeIsNotExists (Code.GetCodeNumber () , 5 , (Code , Last) ->
                                    {
                                        if (confirmedService.getConfirmedIsActiveConfirmed (Code) == null)
                                        {
                                            createCode.set (true);
                                            code.set (Code);
                                            synchronized (wait)
                                            {
                                                wait.notifyAll ();
                                            }
                                            return true;
                                        }
                                        else
                                        {
                                            if (Last)
                                            {
                                                synchronized (wait)
                                                {
                                                    wait.notifyAll ();
                                                }
                                                return true;
                                            }
                                            else return false;
                                        }
                                    })).start ();

                                    synchronized (wait)
                                    {
                                        try
                                        {
                                            wait.wait ();
                                        }
                                        catch (InterruptedException e)
                                        {
                                            l.n (request , routerSC , mainAccount , null , Thread.currentThread ().getStackTrace () , e , null);
                                        }
                                    }
                                    if (createCode.get ())
                                    {
                                        new SendConformCode (email , code.get ());

                                        ConfirmCode confirm = new ConfirmCode ();
                                        confirm.setCode (code.get ());
                                        confirm.setConfirmCodeFor (ConfirmCodeFor.email);
                                        confirm.setMainAccount (mainAccount);
                                        confirm.setSendCodeTo (email);
                                        confirm.setTimeToSendCode (LocalDateTime.now ());
                                        confirm.setTimeToBeOutdated (confirm.getTimeToSendCode ().plusMinutes (min));

                                        confirm = confirmCodeService.Repository.save (confirm);

                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.was_send.name ());
                                        answerToClient.put (AnswerToClient.CUV.id.name () , confirm.getId ());
                                        answerToClient.setReqRes (req , res);
                                        l.n (request , routerSC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , null);
                                        r.nim (req.getRemoteAddr () , mainAccount , typeSC , false);
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.ServerError ();
                                        answerToClient.setReqRes (req , res);
                                        l.n (request , routerSC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , null);
                                        r.nim (req.getRemoteAddr () , mainAccount , typeSC , true);
                                    }
                                }
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.was_send.name ());
                                answerToClient.put (KeyAnswer.shipping_time.name () , Time.toString (sendCode.getTimeToSendCode ()));
                                answerToClient.put (KeyAnswer.validity_time.name () , Time.toString (sendCode.getTimeToBeOutdated ()));
                                answerToClient.setReqRes (req , res);
                                l.n (request , routerSC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.was_send.name ()) , null);
                                r.nim (req.getRemoteAddr () , mainAccount , typeSC , true);
                            }
                        }
                        else
                        {
                            answerToClient = AnswerToClient.ServerError ();
                            answerToClient.setReqRes (req , res);
                            l.n (request , routerSC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , DefaultKey.ce_min_valid.name ());
                            r.nim (req.getRemoteAddr () , mainAccount , typeSC , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.email_found.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (request , routerSC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.email_found.name ()) , null);
                        r.nim (req.getRemoteAddr () , mainAccount , typeSC , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.email_invalid.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (request , routerSC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.email_invalid.name ()) , null);
                    r.nim (req.getRemoteAddr () , mainAccount , typeSC , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (request , routerSC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null);
                r.nim (req.getRemoteAddr () , mainAccount , typeSC , true);
            }
        }
        else answerToClient = cbsil.getAnswerToClient ();

        return answerToClient;
    }

    @RequestMapping (value = { "/confirm" })
    public AnswerToClient confirm
            (HttpServletResponse res , HttpServletRequest req ,
             @RequestParam ("email") String email ,
             @RequestParam ("id") String strId ,
             @RequestParam ("code") String code ,
             @CookieValue (name = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        String request = ToJson.CreateClass.n ("email" , email).put ("id_str" , strId).put ("code" , code).toJson ();
        CBSIL cbsil = CBSIL.BSubmitRequest (request , routerSC , req , res , typeSC , codeLogin , userLoginService);

        if (cbsil.isOk ())
        {
            MainAccount mainAccount = null;
            if (cbsil.getIsLogin () != null) mainAccount = cbsil.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if ((!Str.IsEmpty (email)) && (!Str.IsEmpty (strId)) && (!Str.IsEmpty (code)))
            {
                ID id = new ID (strId);
                if (id.isValid ())
                {
                    if (code.matches ("[0-9]*"))
                    {
                        VEmail vEmail = new VEmail (email);
                        if (vEmail.check ())
                        {
                            ConfirmCode findCode = confirmCodeService.Repository.findCode (id.getId () , code , ConfirmCodeFor.email , email , LocalDateTime.now ());
                            if (findCode != null)
                            {
                                Confirmed confirmed = confirmedService.Repository.findByValueAndActiveTrue (email);

                                if (confirmed != null)
                                {
                                    confirmed.setActive (false);

                                    ConfirmCode confirmCode = confirmed.getConfirmCode ();
                                    confirmCode.setDeleted (true);
                                    confirmCode.setDeletedAt (LocalDateTime.now ());
                                    confirmCodeService.Repository.save (confirmCode);

                                    confirmedService.Repository.save (confirmed);
                                }

                                Confirmed confirm = new Confirmed ();
                                confirm.setValue (email);
                                confirm.setActive (true);
                                confirm.setCode (Code.CreateCodeLogin ());
                                confirm.setConfirmCode (findCode);
                                confirm.setConfirmedFor (ConfirmCodeFor.email);

                                confirmedService.Repository.save (confirm);


                                findCode.setUsing (true);
                                findCode.setConfirmed (true);
                                findCode.setTimeToConfirmed (LocalDateTime.now ());
                                confirmCodeService.Repository.save (findCode);

                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.confirmed.name ());
                                answerToClient.put (ValAnswer.code.name () , confirm.getCode ());
                                answerToClient.setReqRes (req , res);
                                l.n (request , routerSC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.confirmed.name ());
                                r.nim (req.getRemoteAddr () , mainAccount , typeC , false);
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (request , routerC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid.name ()) , null);
                                r.nim (req.getRemoteAddr () , mainAccount , typeC , true);
                            }
                        }
                        else
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.email_invalid.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (request , routerC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.email_invalid.name ()) , null);
                            r.nim (req.getRemoteAddr () , mainAccount , typeC , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.code_invalid.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (request , routerC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.code_invalid.name ()) , null);
                        r.nim (req.getRemoteAddr () , mainAccount , typeC , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid ();
                    answerToClient.setReqRes (req , res);
                    l.n (request , routerC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.id_invalid.name ()) , null);
                    r.nim (req.getRemoteAddr () , mainAccount , typeC , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (request , routerC , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null);
                r.nim (req.getRemoteAddr () , mainAccount , typeC , true);
            }
        }
        else answerToClient = cbsil.getAnswerToClient ();

        return answerToClient;
    }

    private enum ValAnswer
    {
        email_invalid, email_found, was_send, code_invalid, invalid, confirmed, code
    }

    private enum KeyAnswer
    {
        shipping_time, validity_time
    }

}
