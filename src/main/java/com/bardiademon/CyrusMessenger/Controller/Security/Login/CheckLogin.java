package com.bardiademon.CyrusMessenger.Controller.Security.Login;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginRepository;
import com.bardiademon.CyrusMessenger.Model.VCodeLogin;


public class CheckLogin
{
    private AnswerToClient answerToClient;

    private VCodeLogin vCodeLogin;

    private boolean valid;
    private String codeLogin;
    private UserLoginRepository userLoginRepository;

    public CheckLogin (String CodeLogin , UserLoginRepository _UserLoginRepository)
    {
        this.codeLogin = CodeLogin;
        this.userLoginRepository = _UserLoginRepository;
        check ();
    }

    private void check ()
    {
        if (codeLogin.equals (""))
        {
            valid = false;
            answerToClient = AnswerToClient.NotLoggedIn ();
            return;
        }

        vCodeLogin = new VCodeLogin ();
        valid = (vCodeLogin.IsValid (userLoginRepository , codeLogin));

        if (!valid) answerToClient = AnswerToClient.NotLoggedIn ();
    }

    public boolean isValid ()
    {
        return valid;
    }

    public AnswerToClient getAnswerToClient ()
    {
        return answerToClient;
    }

    public VCodeLogin getVCodeLogin ()
    {
        return vCodeLogin;
    }
}
