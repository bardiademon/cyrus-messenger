package com.bardiademon.CyrusMessenger.Controller.Rest.RestRegister;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedByTheSystemService;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedFor;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ConfirmedPhone.ConfirmedPhone;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ConfirmedPhone.ConfirmedPhoneService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.FindInTheDatabase.FITD_Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = Domain.RNRegister.RN_MAIN, method = RequestMethod.POST)
public class RestRegister
{

    private AnswerToClient answerToClient;

    private RegisterRequest registerRequest;

    private final MainAccountService mainAccountService;
    private ConfirmedPhoneService confirmedPhoneService;
    private ConfirmCodeService confirmCodeService;
    private SubmitRequestService submitRequestService;
    private BlockedByTheSystemService blockedByTheSystemService;
    private HttpServletRequest request;

    @Autowired
    public RestRegister (MainAccountService _MainAccountService ,
                         ConfirmedPhoneService _ConfirmedPhoneService ,
                         ConfirmCodeService _ConfirmCodeService ,
                         SubmitRequestService _SubmitRequestService ,
                         BlockedByTheSystemService _BlockedByTheSystemService
    )
    {
        this.mainAccountService = _MainAccountService;
        this.confirmedPhoneService = _ConfirmedPhoneService;
        this.confirmCodeService = _ConfirmCodeService;
        this.submitRequestService = _SubmitRequestService;
        this.blockedByTheSystemService = _BlockedByTheSystemService;
    }

    @RequestMapping ({"/" , ""})
    public AnswerToClient register (HttpServletResponse res , HttpServletRequest request , @RequestBody RegisterRequest registerRequest)
    {
        this.request = request;
        CheckBlockSystem checkBlockSystem;
        if ((checkBlockSystem = new CheckBlockSystem (request , blockedByTheSystemService , BlockedFor.submit_request , SubmitRequestType.register.name ())).isBlocked ())
            answerToClient = checkBlockSystem.getAnswerToClient ();
        else
        {
            this.registerRequest = registerRequest;

            if (isNull ())
            {
                answerToClient = AnswerToClient.error400 ();
                answerToClient.put (KeyAnswer.answer.name () , ValAnswer.request_is_null.name ());
                submitRequestService.newRequest (request.getRemoteAddr () , SubmitRequestType.register , true);
            }
            else
            {
                String emptyName = ValAnswer.empty.name ();
                if (isEmpty (registerRequest.getUsername ())) setError400 (ValAnswer.username.name () , emptyName);
                if (isEmpty (registerRequest.getPassword ())) setError400 (ValAnswer.password.name () , emptyName);
                else if (isEmpty (registerRequest.getName ())) setError400 (ValAnswer.name.name () , emptyName);
                else if (isEmpty (registerRequest.getFamily ())) setError400 (ValAnswer.family.name () , emptyName);
                else if (isEmpty (registerRequest.getCodeConfirmedPhone ()))
                {
                    submitRequestService.newRequest (request.getRemoteAddr () , SubmitRequestType.register , true);
                    setError400 (ValAnswer.code_confirmed_phone.name () , emptyName);
                }
                else
                {
                    if ((new VUsername (registerRequest.getUsername ())).check ())
                    {
                        if (!checkExists ()) return answerToClient;
                        else
                        {
                            ConfirmedPhone confirmedPhone = checkCodeConfirmedPhone (registerRequest.getCodeConfirmedPhone ());
                            if (confirmedPhone == null)
                                setError400 (ValAnswer.code_confirmed_phone.name () , ValAnswer.invalid.name ());
                            else
                            {
                                MainAccount mainAccount = confirmedPhone.getConfirmCode ().getMainAccount ();
                                if (mainAccount == null || mainAccount.isDeleted () || !checkExistsPhone (confirmedPhone.getPhone ()))
                                {
                                    if (mainAccountService.newAccount (registerRequest , confirmedPhone , confirmCodeService))
                                    {
                                        answerToClient = new AnswerToClient (200 , true);
                                        answerToClient.put (KeyAnswer.answer.name () , ValAnswer.recorded.name ());
                                        answerToClient.put (KeyAnswer.phone.name () , confirmedPhone.getPhone ());
                                        submitRequestService.newRequest (request.getRemoteAddr () , SubmitRequestType.register , false);
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.ServerError ();
                                        answerToClient.put (KeyAnswer.answer.name () , ValAnswer.error_recorded.name ());
                                    }
                                }
                                else
                                {
                                    answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED);
                                    answerToClient.put (KeyAnswer.answer.name () , ValAnswer.phone_is_exists.name ());
                                    submitRequestService.newRequest (request.getRemoteAddr () , SubmitRequestType.register , true);
                                }
                            }
                        }
                    }
                    else
                    {
                        setError400 (ValAnswer.username.name () , ValAnswer.invalid.name ());
                        submitRequestService.newRequest (request.getRemoteAddr () , SubmitRequestType.register , true);
                    }
                }
            }
        }
        answerToClient.setResponse (res);
        return answerToClient;
    }

    private boolean checkExistsPhone (String phone)
    {
        return mainAccountService.findPhone (phone) != null;
    }

    private ConfirmedPhone checkCodeConfirmedPhone (String code)
    {
        return confirmedPhoneService.getConfirmedPhoneIsActiveConfirmed (code);
    }

    private boolean isEmpty (String value)
    {
        return value == null || value.equals ("");
    }

    private boolean checkExists ()
    {
        FITD_Username fitd_username = new FITD_Username (registerRequest.getUsername () , mainAccountService);
        if (fitd_username.isFound ())
        {
            setError400 (ValAnswer.username.name () , ValAnswer.exists.name ());
            return false;
        }
        return true;
    }

    private void setError400 (String what , String is)
    {
        answerToClient = AnswerToClient.error400 ();
        answerToClient.put (KeyAnswer.answer.name () , String.format ("%s_is_%s" , what , is));
        submitRequestService.newRequest (request.getRemoteAddr () , SubmitRequestType.register , true);
    }

    private boolean isNull ()
    {
        return (registerRequest.getFamily () == null || registerRequest.getName () == null || registerRequest.getUsername () == null);
    }

    private enum KeyAnswer
    {
        answer, phone
    }

    private enum ValAnswer
    {
        recorded, error_recorded,
        request_is_null, phone, username, code_confirmed_phone, exists, invalid, empty, password, name, family, phone_is_exists
    }

}