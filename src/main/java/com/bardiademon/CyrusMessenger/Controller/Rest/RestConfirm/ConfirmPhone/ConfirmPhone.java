package com.bardiademon.CyrusMessenger.Controller.Rest.RestConfirm.ConfirmPhone;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient.CUK;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VPhone;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCode;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ConfirmedPhone.ConfirmedPhone;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ConfirmedPhone.ConfirmedPhoneService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.SMS.SendSMS.SendSMSConfirmPhone;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = RouterName.RNConfirm.RN_CONFIRM_PHONE, method = RequestMethod.POST)
public class ConfirmPhone
{

    private static final Object Wait = new Object ();

    private String phone;

    private ConfirmCodeService confirmCodeService;
    private ConfirmedPhoneService confirmedPhoneService;
    private MainAccountService mainAccountService;

    private AnswerToClient answerToClient;

    @Autowired
    public ConfirmPhone (ConfirmCodeService _ConfirmCodeService , ConfirmedPhoneService _ConfirmedPhoneService , MainAccountService _MainAccountService)
    {
        this.confirmCodeService = _ConfirmCodeService;
        this.confirmedPhoneService = _ConfirmedPhoneService;
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient sendCode
            (HttpServletResponse res ,
             @RequestParam ("phone") String phone , @RequestParam ("region") String region)
    {

        if ((phone == null || phone.isEmpty ()) || (region == null || region.isEmpty ()))
            answerToClient = AnswerToClient.RequestIsNull ();
        else
        {
            VPhone vPhone = new VPhone (phone , region);
            if (vPhone.check ())
            {
                this.phone = vPhone.getPhone ();

                ConfirmCode findCode
                        = confirmCodeService.Repository.findCode (ConfirmCodeFor.phone , this.phone , LocalDateTime.now ());

                if (findCode == null)
                {
                    if (isExistsPhone (this.phone))
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.this_phone_has_account.name ());
                    else
                    {
                        new Thread (() -> Code.CreateCodeIsNotExists (Code.GetCodeNumber () , 10 , (code , last) ->
                        {
                            if (confirmCodeService.isExistsCode (code))
                            {
                                if (last)
                                {
                                    answerToClient = AnswerToClient.ServerError ();
                                    synchronized (Wait)
                                    {
                                        Wait.notify ();
                                    }
                                }
                                return last;
                            }
                            else
                            {
                                ConfirmCode confirmCode = new ConfirmCode ();
                                confirmCode.setSendCodeTo (ConfirmPhone.this.phone);
                                confirmCode.setCode (code);
                                confirmCode.setConfirmCodeFor (ConfirmCodeFor.phone);
                                confirmCode.setTimeToSendCode (LocalDateTime.now ());
                                confirmCode.setTimeToBeOutdated (LocalDateTime.now ().plusMinutes (15));

                                confirmCode = confirmCodeService.Repository.save (confirmCode);
                                if (confirmCode.getId () > 0)
                                {
                                    new SendSMSConfirmPhone ("Quest" , ConfirmPhone.this.phone);
                                    answerToClient = AnswerToClient.OK ();
                                    answerToClient.put (CUK.answer.name () , ValAnswer.code_send.name ());
                                    answerToClient.put (KeyAnswer.to.name () , ConfirmPhone.this.phone);
                                    answerToClient.put (KeyAnswer.id.name () , confirmCode.getId ());
                                    answerToClient.put (KeyAnswer.waiting_time.name () , Time.getTime (confirmCode.getTimeToBeOutdated ()));
                                }
                                else
                                {
                                    answerToClient = AnswerToClient.ServerError ();
                                }
                                synchronized (Wait)
                                {
                                    Wait.notify ();
                                }
                            }
                            return true;
                        })).start ();
                        synchronized (Wait)
                        {
                            try
                            {
                                Wait.wait ();
                            }
                            catch (InterruptedException e)
                            {
                                answerToClient = AnswerToClient.ServerError ();
                            }
                        }
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.KeyAnswer (AnswerToClient.error400 () ,
                            CUK.answer.name () , ValAnswer.the_code_has_been_sent.name () ,
                            KeyAnswer.to.name () , findCode.getSendCodeTo () ,
                            KeyAnswer.id.name () , findCode.getId () ,
                            KeyAnswer.waiting_time.name () , Time.getTime (findCode.getTimeToBeOutdated ())
                    );
                }
            }
            else
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.phone_invalid.name ());
        }
        answerToClient.setResponse (res);
        return answerToClient;
    }

    private boolean isExistsPhone (String phone)
    {
        return mainAccountService.findPhone (phone) != null;
    }

    @RequestMapping (value = "/confirm")
    private AnswerToClient confirmCode
            (HttpServletResponse res ,
             @RequestParam ("phone") String phone ,
             @RequestParam ("region") String region ,
             @RequestParam ("code") String code ,
             @RequestParam ("id") int id)
    {
        AnswerToClient answerToClient;

        if ((phone == null || phone.isEmpty ()) || (region == null || region.isEmpty ()) || (code == null || code.isEmpty ()) || id <= 0)
            answerToClient = AnswerToClient.RequestIsNull ();
        else
        {
            VPhone vPhone = new VPhone (phone , region);
            if (vPhone.check ())
            {
                phone = vPhone.getPhone ();
                ConfirmCode findCode
                        = confirmCodeService.Repository.findCode
                        (id , code , ConfirmCodeFor.phone , phone , LocalDateTime.now ());
                if (findCode != null)
                {
                    confirmed (findCode);
                    if (this.code != null)
                    {
                        answerToClient = AnswerToClient.error400 ();
                        answerToClient.put (CUK.answer.name () , ValAnswer.confirmed.name ());
                        answerToClient.put (KeyAnswer.phone.name () , this.phone);
                        answerToClient.put (KeyAnswer.code_confirmed_phone.name () , this.code);
                    }
                    else
                        answerToClient = AnswerToClient.ServerError ();
                }
                else
                {
                    answerToClient = AnswerToClient.error400 ();
                    answerToClient.put (CUK.answer.name () , ValAnswer.code_invalid.name ());
                }
            }
            else
            {
                answerToClient = AnswerToClient.error400 ();
                answerToClient.put (CUK.answer.name () , ValAnswer.phone_invalid.name ());
            }
        }
        answerToClient.setResponse (res);
        return answerToClient;
    }

    private String code;

    private void confirmed (ConfirmCode confirmCode)
    {
        new Thread (() -> Code.CreateCodeIsNotExists (Code.CreateCodeLong () , 10 , (code , last) ->
        {
            if (confirmedPhoneService.hasCode (code))
            {
                if (last)
                {
                    synchronized (Wait)
                    {
                        Wait.notify ();
                    }
                }
                return last;
            }
            else
            {
                ConfirmPhone.this.code = code;
                synchronized (Wait)
                {
                    Wait.notify ();
                }
                return true;
            }
        })).start ();

        synchronized (Wait)
        {
            try
            {
                Wait.wait ();
            }
            catch (InterruptedException ignored)
            {
            }
        }
        if (code != null)
        {
            ConfirmedPhone oldConfirmed = confirmedPhoneService.Repository.findByPhoneAndActiveTrue (this.phone);

            if (oldConfirmed != null)
                oldConfirmed.setActive (false);

            confirmCode.setConfirmed (true);
            confirmCode.setUsing (true);
            confirmCode.setTimeToConfirmed (LocalDateTime.now ());

            ConfirmedPhone confirmedPhone = new ConfirmedPhone ();
            confirmedPhone.setActive (true);
            confirmedPhone.setConfirmCode (confirmCode);
            confirmedPhone.setCode (code);
            confirmedPhone.setPhone (this.phone);

            confirmCodeService.Repository.save (confirmCode);
            if (oldConfirmed != null) confirmedPhoneService.Repository.save (oldConfirmed);
            confirmedPhoneService.Repository.save (confirmedPhone);
        }
    }

    private enum KeyAnswer
    {
        to, waiting_time, id, code_confirmed_phone, phone
    }

    private enum ValAnswer
    {
        phone_invalid, code_send, the_code_has_been_sent, confirmed, code_invalid, this_phone_has_account
    }

}