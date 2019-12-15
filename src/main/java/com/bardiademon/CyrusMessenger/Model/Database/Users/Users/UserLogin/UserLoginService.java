package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


@Service
public class UserLoginService
{

    private LocalDateTime creditUp;

    public final UserLoginRepository Repository;

    @Autowired
    public UserLoginService (UserLoginRepository _UserLoginRepository)
    {
        this.Repository = _UserLoginRepository;
    }

    public void loginFailed (String ip)
    {
        UserLogin userLogin = new UserLogin ();
        userLogin.setIp (ip);
        userLogin.setSuccessful (false);
        Repository.save (userLogin);
    }

    public boolean newLogin (String code , MainAccount mainAccount , String ip)
    {
        UserLogin userLogin = new UserLogin ();
        userLogin.setIp (ip);
        userLogin.setCodeLogin (code);
        userLogin.setMainAccount (mainAccount);

        final int CREADIT_UP = 1;
        creditUp = LocalDateTime.now ().plusDays (CREADIT_UP);
        userLogin.setCreditUp (creditUp);
        userLogin.setSuccessful (true);
        return ((Repository.save (userLogin)) != null);
    }

    /**
     * validUEP => valid Username , Email , Phone
     */
    public void validUEP (String ip)
    {
        UserLogin userLogin = new UserLogin ();
        userLogin.setIp (ip);
        userLogin.setSuccessful (true);
        Repository.save (userLogin);
    }

    public String getCreditUp ()
    {
        return creditUp.format (DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm:ss"));
    }
}
