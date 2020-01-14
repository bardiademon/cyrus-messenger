package com.bardiademon.CyrusMessenger.Controller.Security.Login;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginRepository;
import com.bardiademon.CyrusMessenger.Model.VCodeLogin;

import java.time.LocalDateTime;

public class Logout
{
    private UserLoginRepository userLoginRepository;
    private String codeLogin;

    public Logout (UserLoginRepository _UserLoginRepository , String CodeLogin)
    {
        this.userLoginRepository = _UserLoginRepository;
        this.codeLogin = CodeLogin;
        logout ();
    }

    private void logout ()
    {
        VCodeLogin vCodeLogin = new VCodeLogin ();
        if (vCodeLogin.IsValid (userLoginRepository , codeLogin))
        {
            UserLogin userLogin = vCodeLogin.getUserLogin ();
            userLogin.setTimeLogout (LocalDateTime.now ());
            userLoginRepository.save (userLogin);
        }
    }
}
