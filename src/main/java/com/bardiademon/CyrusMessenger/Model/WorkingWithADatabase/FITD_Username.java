package com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.Usernames;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;

// FITD => Find In The Database
public final class FITD_Username
{
    private final String username;
    private final UsernamesService usernamesService;

    private MainAccount mainAccount;
    private boolean valid;
    private boolean found;

    private AnswerToClient answer;

    public FITD_Username (String Username , UsernamesService _UsernamesService)
    {
        this.username = Username;
        this.usernamesService = _UsernamesService;
        if (validation ()) found = foundUsername ();
        else answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.username_invalid.name ());
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
            answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.username_not_found.name ());
            return false;
        }
    }

    private enum ValAnswer
    {
        username_not_found, username_invalid
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

    public AnswerToClient getAnswer ()
    {
        return answer;
    }
}
