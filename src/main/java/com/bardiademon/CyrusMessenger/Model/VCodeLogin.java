package com.bardiademon.CyrusMessenger.Model;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginRepository;

import java.time.LocalDateTime;

public final class VCodeLogin
{
    private UserLogin userLogin;

    public boolean IsValid (UserLoginRepository repository , String codeLogin)
    {
        userLogin = repository.findValidCode (codeLogin , LocalDateTime.now ().plusMinutes (5));
        return userLogin != null;
    }

    public UserLogin getUserLogin ()
    {
        return userLogin;
    }

    public MainAccount getMainAccount ()
    {
        return userLogin.getMainAccount ();
    }
}
