package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Contacts.NewContact;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContacts;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNContacts.RN_NEW_CONTACT, method = RequestMethod.POST)
public final class NewContact
{

    private List<UserContacts> contacts;
    private List<UserContacts> newContacts;
    private List<UserContacts> contactsAnswer;

    private UserLoginService userLoginService;
    private MainAccountService mainAccountService;
    private UserContactsService userContactsService;

    public NewContact (UserLoginService _UserLoginService , MainAccountService _MainAccountService , UserContactsService _UserContactsService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.userContactsService = _UserContactsService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient newContact
            (HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody List<UserContacts> contacts)
    {
        AnswerToClient answerToClient;

        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
        {
            this.contacts = contacts;
            newContacts (isLogin.getVCodeLogin ().getMainAccount ());
            if (contactsAnswer.size () == 0) answerToClient = AnswerToClient.RequestIsNull ();
            else
            {
                if (newContacts != null && newContacts.size () > 0)
                    userContactsService.Repository.saveAll (newContacts);

                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , contactsAnswer);
            }
        }
        else answerToClient = isLogin.getAnswerToClient ();

        answerToClient.setResponse (res);

        return answerToClient;
    }

    private void newContacts (MainAccount mainAccount)
    {
        if (contacts == null || contacts.size () == 0) return;
        long lenPhone;
        UserContacts contact, userContacts;
        MainAccount thisFor;

        contactsAnswer = new ArrayList<> ();
        newContacts = new ArrayList<> ();
        for (int i = 0, len = contacts.size (); i < len; i++)
        {
            userContacts = contacts.get (i);

            if (userContacts == null) return;

            if (userContacts.getPhone ().substring (0 , 1).equals ("0"))
                userContacts.setPhone (userContacts.getPhone ().substring (1));

            if (Str.IsEmpty (userContacts.getPhone ()) || Str.IsEmpty (userContacts.getName ()))
                userContacts.setMessage (ValAnswer.name_or_phone_is_empty.name ());

            else if ((lenPhone = userContacts.getPhone ().length ()) < UserContacts.MIN_PHONE || lenPhone > UserContacts.MAX_PHONE)
                userContacts.setMessage (ValAnswer.phone_length_invalid.name ());

            else if (!userContacts.getPhone ().matches ("[0-9]*"))
                userContacts.setMessage (ValAnswer.phone_invalid_value.name ());

            else if (has (userContacts.getPhone () , false , i))
                userContacts.setMessage (ValAnswer.phone_exists_in_request.name ());

            else
            {
                contact = userContactsService.hasPhone (userContacts.getPhone ());

                if (contact != null)
                {
                    if (Str.IsEmpty (userContacts.getNewPhone ()))
                    {
                        userContacts.setMessage (ValAnswer.new_phone_is_empty.name ());
                        contactsAnswer.add (userContacts);
                        continue;
                    }
                    else
                    {
                        if (!Str.IsEmpty (userContacts.getNewPhone ()) && userContacts.getNewPhone ().substring (0 , 1).equals ("0"))
                            userContacts.setNewPhone (userContacts.getNewPhone ().substring (1));

                        if (has (userContacts.getNewPhone () , true , i))
                        {
                            userContacts.setMessage (ValAnswer.new_phone_exists_in_request.name ());
                            contactsAnswer.add (userContacts);
                            continue;
                        }
                        else if (userContactsService.hasPhone (userContacts.getNewPhone ()) != null)
                        {
                            userContacts.setMessage (ValAnswer.has_new_phone.name ());
                            contactsAnswer.add (userContacts);
                            continue;
                        }
                    }

                    userContacts.setMessage (ValAnswer.updated.name ());

                    thisFor = mainAccountService.findPhoneLike (userContacts.getNewPhone ());

                    if (thisFor == null)
                    {
                        contact.setMainAccountContact (null);
                        contact.setPhone (userContacts.getNewPhone ());
                    }
                    else
                    {
                        contact.setPhone (null);
                        contact.setMainAccountContact (thisFor);
                    }
                }
                else
                {
                    contact = new UserContacts ();
                    userContacts.setMessage (ValAnswer.inserted.name ());
                    userContacts.setNewPhone (null);

                    thisFor = mainAccountService.findPhoneLike (userContacts.getPhone ());

                    if (thisFor == null)
                    {
                        contact.setMainAccountContact (null);
                        contact.setPhone (userContacts.getPhone ());
                    }
                    else
                    {
                        contact.setMainAccountContact (thisFor);
                        contact.setPhone (null);
                    }
                }

                contact.setName (userContacts.getName ());

                contact.setMainAccount (mainAccount);

                contact.setSuccessfully (true);
                userContacts.setSuccessfully (true);

                newContacts.add (contact);
            }
            contactsAnswer.add (userContacts);
        }
    }

    private boolean has (String phone , boolean newPhone , int index)
    {
        UserContacts userContacts;
        for (int i = 0, len = contacts.size (); i < len; i++)
        {
            if (i != index)
            {
                userContacts = contacts.get (i);
                if (newPhone && userContacts.getNewPhone ().equals (phone)) return true;
                else if (!newPhone && userContacts.getPhone ().equals (phone)) return true;
            }
        }
        return false;
    }

    public enum ValAnswer
    {
        name_or_phone_is_empty, phone_length_invalid, phone_invalid_value,
        phone_exists_in_request, new_phone_exists_in_request, updated, inserted, new_phone_is_empty, has_new_phone
    }

}
