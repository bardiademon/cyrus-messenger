package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public final class UserLoginService
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

    public ResNewLogin newLogin (String code , MainAccount mainAccount , String ip)
    {
        UserLogin userLogin = new UserLogin ();
        userLogin.setIp (ip);
        userLogin.setCodeLogin (code);
        userLogin.setMainAccount (mainAccount);
        userLogin.setTimeLogin (LocalDateTime.now ());

        ResNewLogin resNewLogin = new ResNewLogin ();
        resNewLogin.setCreditUp (LocalDateTime.now ().plusDays (1));

        userLogin.setCreditUp (resNewLogin.getCreditUp ());
        userLogin.setSuccessful (true);

        resNewLogin.setLogin ((Repository.save (userLogin)).getId () > 0);

        return resNewLogin;
    }

    public static class ResNewLogin
    {
        private boolean login;
        private LocalDateTime creditUp;

        private ResNewLogin ()
        {
        }

        public boolean isLogin ()
        {
            return login;
        }

        private void setLogin (boolean login)
        {
            this.login = login;
        }

        public LocalDateTime getCreditUp ()
        {
            return creditUp;
        }

        private void setCreditUp (LocalDateTime creditUp)
        {
            this.creditUp = creditUp;
        }
    }

    public boolean logout (String codeLogin)
    {
        UserLogin byCodeLogin = Repository.findByCodeLogin (codeLogin);
        byCodeLogin.setTimeLogout (LocalDateTime.now ());
        UserLogin userLogin = Repository.saveAndFlush (byCodeLogin);
        return ((Repository.getOne (userLogin.getId ()).getTimeLogout ()) != null);
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
