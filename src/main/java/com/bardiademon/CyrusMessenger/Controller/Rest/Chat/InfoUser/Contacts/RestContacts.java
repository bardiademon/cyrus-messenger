package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Contacts;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import static com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Contacts.RequestContacts.MAX_PHONE;
import static com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Contacts.RequestContacts.MIN_PHONE;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import static com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel._Service;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContacts;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNContacts.RN_CONTACTS, method = RequestMethod.POST)
public final class RestContacts
{

    private final UserLoginService userLoginService;

    private final String rAdd, rRemove, rRemoveWithPhone, rList;
    private final SubmitRequestType tAdd, tRemove, tRemoveWithPhone, tList;

    @Autowired
    public RestContacts (UserLoginService _UserLoginService)
    {
        this.userLoginService = _UserLoginService;

        rAdd = Domain.RNChat.RNInfoUser.RNContacts.RN_NEW_CONTACT;
        tAdd = SubmitRequestType.new_contact;

        rRemove = Domain.RNChat.RNInfoUser.RNContacts.RN_REMOVE_CONTACT;
        tRemove = SubmitRequestType.remove_contact;

        rRemoveWithPhone = Domain.RNChat.RNInfoUser.RNContacts.RN_REMOVE_WITH_PHONE_CONTACT;
        tRemoveWithPhone = SubmitRequestType.remove_contact_with_phone;

        rList = Domain.RNChat.RNInfoUser.RNContacts.RN_LIST_CONTACTS;
        tList = SubmitRequestType.list_contact;
    }

    // Start code rest add contacts

    @RequestMapping (value = { "/add" })
    public AnswerToClient add
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody List <RequestContacts> reqLst)
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
                for (RequestContacts request : reqLst)
                {
                    if ((result = checkRequestAdd (request , mainAccount)) == null)
                    {
                        result = new LinkedHashMap <> ();
                        if (_Service.userContactsService.hasPhoneForUser (mainAccount.getId () , request.getPhone ()) == null)
                        {
                            UserContacts contacts = new UserContacts ();
                            contacts.setMainAccount (mainAccount);

                            MainAccount phoneLike = _Service.mainAccountService.findPhoneLike (request.getPhone ());
                            if (phoneLike != null) contacts.setMainAccountContact (phoneLike);

                            contacts.setPhone (request.getPhone ());
                            contacts.setName (request.getName ());
                            contacts.setFamily (request.getFamily ());

                            _Service.userContactsService.Repository.save (contacts);

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

    private Map <String, Object> checkRequestAdd (RequestContacts request , MainAccount mainAccount)
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

    // Start code rest remove contacts

    @RequestMapping (value = { "/remove_with_phone" , "/remove_with_phone/{PHONE_NUMBER}" })
    private AnswerToClient removeWithPhone
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @PathVariable (value = "PHONE_NUMBER", required = false) String phoneNumber)
    {
        AnswerToClient answerToClient;

        String request = ToJson.CreateClass.nj ("phone_number" , phoneNumber);

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , rRemove , tRemove);

        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (!Str.IsEmpty (phoneNumber))
            {
                int lengthPhone = phoneNumber.length ();
                if (lengthPhone >= MIN_PHONE && lengthPhone <= MAX_PHONE)
                {
                    UserContacts contacts = _Service.userContactsService.hasPhoneForUser (mainAccount.getId () , phoneNumber);
                    if (contacts != null)
                    {
                        contacts.setDeleted (true);
                        contacts.setDeletedAt (LocalDateTime.now ());
                        _Service.userContactsService.Repository.save (contacts);

                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.removed.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (request , rRemoveWithPhone , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.removed.name () , tRemoveWithPhone , false);
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.not_found.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (request , rRemoveWithPhone , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null , tRemoveWithPhone , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.phone_number_invalid.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (request , rRemoveWithPhone , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.phone_number_invalid.name ()) , null , tRemoveWithPhone , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , rRemoveWithPhone , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null , tRemoveWithPhone , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();


        return answerToClient;
    }

    @RequestMapping (value = { "/remove" , "/remove/{ID}" })
    private AnswerToClient removeWithId
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @PathVariable (value = "ID", required = false) String strId)
    {
        AnswerToClient answerToClient;

        String request = ToJson.CreateClass.nj ("id" , strId);

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , rRemove , tRemove);

        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (!Str.IsEmpty (strId))
            {
                ID idContacts = new ID (strId);
                if (idContacts.isValid ())
                {
                    UserContacts contacts = _Service.userContactsService.withId (idContacts.getId () , mainAccount.getId ());
                    if (contacts != null)
                    {
                        contacts.setDeleted (true);
                        contacts.setDeletedAt (LocalDateTime.now ());
                        _Service.userContactsService.Repository.save (contacts);

                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.removed.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (request , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.removed.name () , tRemove , false);
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.not_found.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (request , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null , tRemove , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid ();
                    answerToClient.setReqRes (req , res);
                    l.n (null , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.id_invalid.name ()) , null , tRemove , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null , tRemove , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();


        return answerToClient;
    }
    // End code rest remove contacts

    @RequestMapping (value = { "/list_contacts" })
    private AnswerToClient listContacts
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        CBSIL both = CBSIL.Both (null , req , res , codeLogin , userLoginService , rList , tList);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            List <UserContacts> contacts = _Service.userContactsService.listContacts (mainAccount.getId ());
            if (contacts != null && contacts.size () > 0)
            {
                List <RequestContacts> listContacts = new ArrayList <> ();

                MainAccount mainAccountContact;

                UserProfileAccessLevel accessLevel = null;
                for (UserContacts contact : contacts)
                {
                    RequestContacts reqContacts = new RequestContacts ();
                    reqContacts.setName (contact.getName ());
                    reqContacts.setFamily (contact.getFamily ());
                    reqContacts.setPhone (contact.getPhone ());

                    if ((mainAccountContact = (contact.getMainAccountContact ())) != null)
                    {
                        if (accessLevel == null) accessLevel = new UserProfileAccessLevel (mainAccount);

                        accessLevel.setUser (mainAccountContact);

                        if (accessLevel.hasAccess (Which.find_me) && accessLevel.hasAccess (Which.find_me_by_phone))
                        {
                            reqContacts.setHasAccount (true);

                            if (accessLevel.hasAccess (Which.id))
                                reqContacts.setIdUserContacts (mainAccountContact.getId ());

                        }
                    }

                    listContacts.add (reqContacts);
                }

                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());
                answerToClient.put (KeyAnswer.contacts.name () , listContacts);
                l.n (null , rList , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.found.name () , tList , false);
            }
            else
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found.name ());
                answerToClient.setReqRes (req , res);
                l.n (null , rList , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null , tList , true);
            }

        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum ValAnswer
    {
        // For rest add contacts
        name_is_empty, phone_number_invalid
    }

    private enum KeyAnswer
    {
        // For rest add contacts
        message, len_phone, phone,

        contacts
    }
}
