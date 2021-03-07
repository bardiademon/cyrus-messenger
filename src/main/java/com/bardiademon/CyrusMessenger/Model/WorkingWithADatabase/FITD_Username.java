package com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.Usernames;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;

// FITD => Find In The Database
public final class FITD_Username
{
    private String username;
    private final UsernamesService usernamesService;

    private MainAccount mainAccount;
    private boolean valid;
    private boolean found;

    private AnswerToClient answer;

    public FITD_Username (UsernamesService _UsernamesService)
    {
        this.usernamesService = _UsernamesService;
    }

    public FITD_Username (String Username , UsernamesService _UsernamesService)
    {
        this.usernamesService = _UsernamesService;
        check (Username);
    }

    public void check (String Username)
    {
        this.username = Username;
        if (validation ()) found = foundUsername ();
        else answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.username_invalid);

        if (answer != null) answer.put (KeyAnswer.valusername , Username);
    }

    private boolean validation ()
    {
        return (valid = (new VUsername (username)).check ());
    }

    private boolean foundUsername ()
    {
        Usernames usernames = usernamesService.findForUser (username);
        if (usernames != null)
        {
            this.mainAccount = usernames.getMainAccount ();
            return true;
        }
        else
        {
            answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.username_not_found);
            return false;
        }
    }

    private enum ValAnswer
    {
        username_not_found, username_invalid
    }

    private enum KeyAnswer
    {
        valusername
    }

    public MainAccount getMainAccount ()
    {
        return mainAccount;
    }

    public boolean isValid ()
    {
        return valid;
    }

    public boolean isFound ()
    {
        return found;
    }

    public boolean isOk ()
    {
        return isValid () && isFound ();
    }

    public AnswerToClient getAnswer ()
    {
        return answer;
    }
}
