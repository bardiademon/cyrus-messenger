package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Contacts;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import static com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Contacts.RequestAddContacts.MAX_PHONE;
import static com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Contacts.RequestAddContacts.MIN_PHONE;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContacts;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNContacts.RN_CONTACTS, method = RequestMethod.POST)
public final class RestContacts
{

    private final UserLoginService userLoginService;
    private final UserContactsService userContactsService;
    private final MainAccountService mainAccountService;

    private final String rAdd, rRemove;
    private final SubmitRequestType tAdd, tRemove;

    @Autowired
    public RestContacts
            (UserLoginService _UserLoginService ,
             UserContactsService _UserContactsService ,
             MainAccountService _MainAccountService)
    {
        this.userLoginService = _UserLoginService;
        this.userContactsService = _UserContactsService;
        this.mainAccountService = _MainAccountService;

        rAdd = Domain.RNChat.RNInfoUser.RNContacts.RN_NEW_CONTACT;
        tAdd = SubmitRequestType.new_contact;

        rRemove = Domain.RNChat.RNInfoUser.RNContacts.RN_REMOVE_CONTACT;
        tRemove = SubmitRequestType.remove_contact;

    }

    // Start code rest add contacts

    @RequestMapping (value = { "/add" })
    public AnswerToClient add
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody List <RequestAddContacts> reqLst)
    {
        AnswerToClient answerToClient;

        CBSIL both = CBSIL.Both (reqLst , req , res , codeLogin , userLoginService , rAdd , tAdd);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            if (reqLst != null && reqLst.size () > 0)
            {
                answerToClient = AnswerToClient.OK ();
                answerToClient.setReqRes (req , res);

                Map <String, Object> result;

                List <Map <String, Object>> allResult = new ArrayList <> ();
                boolean added = false;
                for (RequestAddContacts request : reqLst)
                {
                    if ((result = checkRequestAdd (request , mainAccount)) == null)
                    {
                        result = new LinkedHashMap <> ();
                        if (userContactsService.hasPhoneForUser (mainAccount.getId () , request.getPhone ()) == null)
                        {
                            UserContacts contacts = new UserContacts ();
                            contacts.setMainAccount (mainAccount);

                            MainAccount phoneLike = mainAccountService.findPhoneLike (request.getPhone ());
                            if (phoneLike != null) contacts.setMainAccountContact (phoneLike);

                            contacts.setPhone (request.getPhone ());
                            contacts.setName (request.getName ());
                            contacts.setFamily (request.getFamily ());

                            userContactsService.Repository.save (contacts);

                            added = true;

                            l.n (ToJson.To (request) , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.added.name () , tAdd , true);
                        }
                        else
                        {
                            result.put (KeyAnswer.message.name () , AnswerToClient.CUV.found.name ());
                            l.n (ToJson.To (request) , rAdd , mainAccount , null , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.found.name ()) , null , tAdd , true);
                        }
                    }

                    result.put (AnswerToClient.CUV.added.name () , added);
                    result.put (KeyAnswer.phone.name () , request.getPhone ());

                    allResult.add (result);

                    System.gc ();
                }

                if (allResult.size () > 0)
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , allResult);
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (req) , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.found.name ()) , null , tAdd , false);
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.please_try_again.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (req) , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.found.name ()) , null , tAdd , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null , tAdd , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        System.gc ();
        return answerToClient;
    }

    private Map <String, Object> checkRequestAdd (RequestAddContacts request , MainAccount mainAccount)
    {
        Map <String, Object> result = null;

        if (Str.IsEmpty (request.getName ()))
        {
            result = new LinkedHashMap <> ();
            result.put (KeyAnswer.message.name () , ValAnswer.name_is_empty.name ());
            l.n (null , rAdd , mainAccount , null , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.name_is_empty.name ()) , null , tAdd , true);
        }
        else
        {
            int lengthPhone = request.getPhone ().length ();
            if (lengthPhone < MIN_PHONE || lengthPhone > MAX_PHONE)
            {
                result = new LinkedHashMap <> ();
                result.put (KeyAnswer.message.name () , ValAnswer.name_is_empty.name ());

                result.put (KeyAnswer.message.name () , ValAnswer.phone_number_invalid.name ());
                result.put (KeyAnswer.len_phone.name () , lengthPhone);
                result.put (KeyAnswer.message.name () , String.format ("%s >= PHONE <= %s" , MIN_PHONE , MAX_PHONE));
                l.n (null , rAdd , mainAccount , null , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.phone_number_invalid.name ()) , null , tAdd , true);
            }
        }
        System.gc ();

        return result;
    }

    // End code rest add contacts


    private AnswerToClient remove
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        return null;
    }

    private enum ValAnswer
    {
        // For rest add contacts
        name_is_empty, phone_number_invalid
    }

    private enum KeyAnswer
    {

        // For rest add contacts
        message, len_phone, phone
    }
}
