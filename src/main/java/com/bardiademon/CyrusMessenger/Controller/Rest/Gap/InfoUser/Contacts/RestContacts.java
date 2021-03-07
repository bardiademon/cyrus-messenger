package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Contacts;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import static com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Contacts.RequestContacts.MAX_PHONE;
import static com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Contacts.RequestContacts.MIN_PHONE;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import static com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel._Service;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContacts;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.CyrusMap;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@RequestMapping (value = Domain.RNGap.RNInfoUser.RNContacts.RN_CONTACTS, method = RequestMethod.POST)
public final class RestContacts
{

    private final UserLoginService userLoginService;

    private final String rAdd, rRemove, rRemoveWithPhone, rList;
    private final SubmitRequestType tAdd, tRemove, tRemoveWithPhone, tList;

    @Autowired
    public RestContacts (UserLoginService _UserLoginService)
    {
        this.userLoginService = _UserLoginService;

        rAdd = Domain.RNGap.RNInfoUser.RNContacts.RN_NEW_CONTACT;
        tAdd = SubmitRequestType.new_contact;

        rRemove = Domain.RNGap.RNInfoUser.RNContacts.RN_REMOVE_CONTACT;
        tRemove = SubmitRequestType.remove_contact;

        rRemoveWithPhone = Domain.RNGap.RNInfoUser.RNContacts.RN_REMOVE_WITH_PHONE_CONTACT;
        tRemoveWithPhone = SubmitRequestType.remove_contact_with_phone;

        rList = Domain.RNGap.RNInfoUser.RNContacts.RN_LIST_CONTACTS;
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

                CyrusMap <String, Object> result;

                List <Map <String, Object>> allResult = new ArrayList <> ();
                boolean added = false;
                for (RequestContacts request : reqLst)
                {
                    if ((result = checkRequestAdd (request , mainAccount)) == null)
                    {
                        result = new CyrusMap <> ();
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

                            l.n (ToJson.To (request) , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.added , tAdd , true);
                        }
                        else
                        {
                            result.put (KeyAnswer.message , AnswerToClient.CUV.found);
                            added = false;
                            l.n (ToJson.To (request) , rAdd , mainAccount , null , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.found) , tAdd , true);
                        }
                    }

                    result.put (AnswerToClient.CUV.added , added);
                    result.put (KeyAnswer.phone , request.getPhone ());

                    allResult.add (result);

                    System.gc ();
                }

                if (allResult.size () > 0)
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , allResult);
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (req) , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.found) , tAdd , false);
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , AnswerToClient.CUV.please_try_again);
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (req) , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.found) , tAdd , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.request_is_null) , tAdd , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        System.gc ();
        return answerToClient;
    }

    private CyrusMap <String, Object> checkRequestAdd (RequestContacts request , MainAccount mainAccount)
    {
        CyrusMap <String, Object> result = null;

        if (Str.IsEmpty (request.getName ()))
        {
            result = new CyrusMap <> ();
            result.put (KeyAnswer.message , ValAnswer.name_is_empty);
            l.n (null , rAdd , mainAccount , null , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.name_is_empty) , tAdd , true);
        }
        else
        {
            int lengthPhone = request.getPhone ().length ();
            if (lengthPhone < MIN_PHONE || lengthPhone > MAX_PHONE)
            {
                result = new CyrusMap <> ();
//                result.put (KeyAnswer.message.name () , ValAnswer.name_is_empty.name ());

//                result.put (KeyAnswer.message.name () , ValAnswer.phone_number_invalid.name ());
                result.put (KeyAnswer.len_phone , lengthPhone);
                result.put (KeyAnswer.message , String.format ("%s >= PHONE <= %s" , MIN_PHONE , MAX_PHONE));
                l.n (null , rAdd , mainAccount , null , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.phone_number_invalid) , tAdd , true);
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

                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.removed);
                        answerToClient.setReqRes (req , res);
                        l.n (request , rRemoveWithPhone , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.removed , tRemoveWithPhone , false);
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , AnswerToClient.CUV.not_found);
                        answerToClient.setReqRes (req , res);
                        l.n (request , rRemoveWithPhone , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found) , tRemoveWithPhone , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.phone_number_invalid);
                    answerToClient.setReqRes (req , res);
                    l.n (request , rRemoveWithPhone , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.phone_number_invalid) , tRemoveWithPhone , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , rRemoveWithPhone , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.request_is_null) , tRemoveWithPhone , true);
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

                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.removed);
                        answerToClient.setReqRes (req , res);
                        l.n (request , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.removed , tRemove , false);
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , AnswerToClient.CUV.not_found);
                        answerToClient.setReqRes (req , res);
                        l.n (request , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found) , tRemove , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid ();
                    answerToClient.setReqRes (req , res);
                    l.n (null , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.id_invalid) , tRemove , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.request_is_null) , tRemove , true);
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
                    reqContacts.setIdContacts (contact.getId ());
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

                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found);
                answerToClient.put (KeyAnswer.contacts , listContacts);
                l.n (null , rList , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.found , tList , false);
            }
            else
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found);
                answerToClient.setReqRes (req , res);
                l.n (null , rList , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found) , tList , true);
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
