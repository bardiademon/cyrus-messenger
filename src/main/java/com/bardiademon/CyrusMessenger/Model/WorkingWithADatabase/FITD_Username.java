package com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase;

import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;

// FITD => Find In The Database
public final class FITD_Username
{
    private String username;
    private MainAccountService mainAccountService;

    private MainAccount mainAccount;
    private boolean valid;
    private boolean found;

    public FITD_Username (String Username , MainAccountService _MainAccountService)
    {
        this.username = Username;
        this.mainAccountService = _MainAccountService;
        if (validation ()) found = foundUsername ();
    }


    private boolean validation ()
    {
        return (valid = (new VUsername (username)).check ());
    }

    private boolean foundUsername ()
    {
        this.mainAccount = mainAccountService.findUsername (username);
        return this.mainAccount != null;
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


}
