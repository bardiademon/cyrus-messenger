package com.bardiademon.CyrusMessenger.Controller.Rest.RestConfirm.ConfirmPhone;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VPhone;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCode;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UsersStatus.Status;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UsersStatus.UsersStatus;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UsersStatus.UsersStatusService;
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
@RequestMapping (value = RouterName.RNConfirm.RN_CONFIRM_PHONE,method = RequestMethod.POST)
public class ConfirmPhone
{

    private static final Object Wait = new Object ();

    private String phone;

    private MainAccountService mainAccountService;
    private ConfirmCodeService confirmCodeService;
    private UsersStatusService usersStatusService;

    private AnswerToClient answerToClient;

    @Autowired
    public ConfirmPhone (MainAccountService _MainAccountService ,
                         ConfirmCodeService _ConfirmCodeService , UsersStatusService _UsersStatusService)
    {
        this.mainAccountService = _MainAccountService;
        this.confirmCodeService = _ConfirmCodeService;
        this.usersStatusService = _UsersStatusService;
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
                MainAccount phoneNotRegistered = mainAccountService.findPhoneNotRegistered (this.phone);

                if (phoneNotRegistered != null)
                {
                    ConfirmCode findCode
                            = confirmCodeService.Repository.findCode
                            (phoneNotRegistered.getId () , this.phone , LocalDateTime.now () , ConfirmCodeFor.phone);

                    if (findCode == null)
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
                                confirmCode.setMainAccount (phoneNotRegistered);
                                confirmCode.setCode (code);
                                confirmCode.setConfirmCodeFor (ConfirmCodeFor.phone);
                                confirmCode.setTimeToSendCode (LocalDateTime.now ());
                                confirmCode.setTimeToBeOutdated (LocalDateTime.now ().plusMinutes (15));

                                confirmCode = confirmCodeService.Repository.save (confirmCode);
                                if (confirmCode.getId () > 0)
                                {
                                    new SendSMSConfirmPhone (phoneNotRegistered.getName () , ConfirmPhone.this.phone);
                                    answerToClient = AnswerToClient.OK ();
                                    answerToClient.put (KeyAnswer.answer.name () , ValAnswer.code_send.name ());
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
                    else
                    {
                        answerToClient = AnswerToClient.error400 ();
                        answerToClient.put (KeyAnswer.answer.name () , ValAnswer.the_code_has_been_sent.name ());
                        answerToClient.put (KeyAnswer.to.name () , findCode.getSendCodeTo ());
                        answerToClient.put (KeyAnswer.id.name () , findCode.getId ());
                        answerToClient.put (KeyAnswer.waiting_time.name () , Time.getTime (findCode.getTimeToBeOutdated ()));
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.error400 ();
                    answerToClient.put (KeyAnswer.answer.name () , ValAnswer.phone_not_found.name ());
                }
            }
            else
            {
                answerToClient = AnswerToClient.error400 ();
                answerToClient.put (KeyAnswer.answer.name () , ValAnswer.phone_in_valid.name ());
            }
        }
        answerToClient.setResponse (res);
        return answerToClient;
    }

    @RequestMapping (value = "/confirm")
    private AnswerToClient confirmCode
            (HttpServletResponse res ,
             @RequestParam ("phone") String phone ,
             @RequestParam ("region") String region ,
             @RequestParam ("code") String code ,
             @RequestParam ("id") int id
            )
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
                    if (confirmed (findCode))
                    {
                        answerToClient = AnswerToClient.error400 ();
                        answerToClient.put (KeyAnswer.answer.name () , ValAnswer.confirmed.name ());
                    }
                    else
                        answerToClient = AnswerToClient.ServerError ();
                }
                else
                {
                    answerToClient = AnswerToClient.error400 ();
                    answerToClient.put (KeyAnswer.answer.name () , ValAnswer.code_invalid.name ());
                }
            }
            else
            {
                answerToClient = AnswerToClient.error400 ();
                answerToClient.put (KeyAnswer.answer.name () , ValAnswer.phone_in_valid.name ());
            }
        }
        answerToClient.setResponse (res);
        return answerToClient;
    }

    private boolean confirmed (ConfirmCode confirmCode)
    {
        MainAccount mainAccount
                = mainAccountService.findPhoneNotRegistered (confirmCode.getSendCodeTo ());
        if (mainAccount != null)
        {
            confirmCode.setMainAccount (mainAccount);
            confirmCode.setConfirmed (true);
            confirmCode.setUsing (true);
            confirmCode.setTimeToConfirmed (LocalDateTime.now ());

            mainAccount.setRegistered (true);
            mainAccount.setRegisteredAt (LocalDateTime.now ());
            mainAccount.setActive (true);


            UsersStatus usersStatus = new UsersStatus ();
            usersStatus.setMainAccount (mainAccount);
            usersStatus.setConfirmCode (confirmCode);
            usersStatus.setActiveRow (true);
            usersStatus.setStatus (Status.conformed_phone);

            mainAccountService.Repository.save (mainAccount);
            confirmCodeService.Repository.save (confirmCode);
            usersStatusService.Repository.save (usersStatus);
            return true;
        }
        else return false;
    }

    private enum KeyAnswer
    {
        answer, to, waiting_time, id
    }

    private enum ValAnswer
    {
        phone_in_valid, phone_not_found, code_send, the_code_has_been_sent, confirmed, code_invalid
    }

}
