package com.bardiademon.CyrusMessenger.Controller.Rest.RestRegister;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VEmail;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VPhone;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.FindInTheDatabase.FITD_Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = "/api/register", method = RequestMethod.POST)
public class RestRegister
{

    private AnswerToClient answerToClient;

    private RegisterRequest registerRequest;

    private final MainAccountService mainAccountService;

    @Autowired
    public RestRegister (MainAccountService _MainAccountService)
    {
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping ({"/" , ""})
    public AnswerToClient register (HttpServletResponse res , @RequestBody RegisterRequest registerRequest)
    {
        this.registerRequest = registerRequest;

        if (isNull ())
        {
            answerToClient = AnswerToClient.error400 ();
            answerToClient.put ("answer" , "Request is null");
        }
        else
        {
            if (isEmpty (registerRequest.username)) setError400 ("Username" , "empty");
            if (isEmpty (registerRequest.password)) setError400 ("Password" , "empty");
            else if (isEmpty (registerRequest.region)) setError400 ("Region" , "empty");
            else if (isEmpty (registerRequest.getPhone ())) setError400 ("Phone" , "empty");
            else if (isEmpty (registerRequest.name)) setError400 ("Name" , "empty");
            else if (isEmpty (registerRequest.family)) setError400 ("Family" , "empty");
            else
            {
                if ((new VUsername (registerRequest.username)).check ())
                {
                    VPhone vPhone;
                    if ((vPhone = new VPhone (registerRequest.getPhone () , registerRequest.region)).check ())
                    {
                        registerRequest.setPhone (vPhone.getPhone ());
                        if (registerRequest.email.equals ("") || new VEmail (registerRequest.email).check ())
                        {
                            if (!checkExists ()) return answerToClient;
                            else
                            {
                                if (mainAccountService.newAccount (registerRequest))
                                {
                                    answerToClient = new AnswerToClient (200 , true);
                                    answerToClient.put ("answer" , "Recorded");
                                }
                                else
                                {
                                    answerToClient = new AnswerToClient (500 , false);
                                    answerToClient.put ("answer" , "Error record");
                                }
                            }
                        }
                        else setError400 ("Email" , "invalid");
                    }
                    else setError400 ("Phone" , "invalid");

                }
                else setError400 ("Username" , "invalid");
            }
        }
        answerToClient.setResponse (res);
        return answerToClient;
    }


    private boolean isEmpty (String value)
    {
        return value == null || value.equals ("");
    }

    private boolean checkExists ()
    {
        FITD_Username fitd_username = new FITD_Username (registerRequest.username , mainAccountService);
        if (fitd_username.isFound ())
        {
            setError400 ("Username" , "Exists");
            return false;
        }
        else
        {
            MainAccount mainAccount = mainAccountService.Repository.findByPhone (registerRequest.getPhone ());
            if (mainAccount != null)
            {
                setError400 ("Phone" , "Exists");
                return false;
            }
            else
            {
                if (registerRequest.email == null) return true;

                mainAccount = mainAccountService.Repository.findByEmail (registerRequest.email);
                if (mainAccount != null)
                {
                    setError400 ("Email" , "Exists");
                    return false;
                }
            }
        }
        return true;
    }

    private void setError400 (String what , String is)
    {
        answerToClient = AnswerToClient.error400 ();
        answerToClient.put ("answer" , String.format ("%s is %s" , what , is));
    }

    private boolean isNull ()
    {
        return (registerRequest.email == null || registerRequest.family == null || registerRequest.name == null || registerRequest.getPhone () == null || registerRequest.username == null);
    }

}