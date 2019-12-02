package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserLoginService
{

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

}
