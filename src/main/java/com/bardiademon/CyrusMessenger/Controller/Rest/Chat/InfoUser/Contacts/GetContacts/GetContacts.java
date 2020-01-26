package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Contacts.GetContacts;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContacts;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping (value = RouterName.RNChat.RNInfoUser.RNContacts.RN_GET_CONTACT, method = RequestMethod.POST)
public final class GetContacts
{

    private UserLoginService userLoginService;
    private MainAccountService mainAccountService;
    private UserContactsService userContactsService;

    public GetContacts (UserLoginService _UserLoginService , MainAccountService _MainAccountService , UserContactsService _UserContactsService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.userContactsService = _UserContactsService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient getContact
            (HttpServletResponse res ,
             @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody List<UserContacts> contacts)
    {
        AnswerToClient answerToClient;

        CheckLogin checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
        if (checkLogin.isValid ())
        {
            List<UserContacts> getContacts;

            boolean valid = false;

            if (contacts == null || contacts.size () == 0)
            {
                contacts = checkLogin.getVCodeLogin ().getMainAccount ().getUserContacts ();
                valid = true;
            }

            getContacts = new ArrayList<> ();
            UserContacts userContacts = null;
            long idContact;
            for (UserContacts contact : contacts)
            {
                if (!valid)
                    userContacts = userContactsService.hasPhone (contact.getPhone ());
                else userContacts = contact;

                if (valid || userContacts != null)
                {
                    if (Str.IsEmpty (userContacts.getPhone ()))
                    {
                        idContact = userContacts.getMainAccountContact ().getId ();
                        if (idContact > 0)
                        {
                            userContacts.setPhone (mainAccountService.Repository.findByIdAndDeletedFalse (idContact).getPhone ());
                            getContacts.add (setFound (userContacts));
                        }
                        else getContacts.add (setNotFound (contact));
                    }
                    else getContacts.add (setFound (userContacts));
                }
                else
                    getContacts.add (setNotFound (contact));
            }
            if (getContacts.size () == 0)
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.not_found);
            else answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , getContacts);

        }
        else answerToClient = checkLogin.getAnswerToClient ();

        answerToClient.setResponse (res);
        return answerToClient;
    }

    private UserContacts setFound (UserContacts userContacts)
    {
        userContacts.setMessage (ValAnswer.is_found.name ());
        userContacts.setSuccessfully (true);
        return userContacts;
    }

    private UserContacts setNotFound (UserContacts userContacts)
    {
        userContacts.setMessage (ValAnswer.not_found.name ());
        return userContacts;
    }

    private enum ValAnswer
    {
        is_found, not_found
    }

}