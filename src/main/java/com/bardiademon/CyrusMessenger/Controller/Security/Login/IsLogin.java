package com.bardiademon.CyrusMessenger.Controller.Security.Login;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginRepository;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.VCodeLogin;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;

public final class IsLogin
{
    private AnswerToClient answerToClient;

    private VCodeLogin vCodeLogin;

    private boolean valid;
    private final String codeLogin;
    private final UserLoginRepository userLoginRepository;

    public IsLogin (String CodeLogin)
    {
        this (CodeLogin , (ThisApp.Context ().getBean (UserLoginService.class)).Repository);
    }

    public IsLogin (String CodeLogin , UserLoginRepository _UserLoginRepository)
    {
        this.codeLogin = CodeLogin;
        this.userLoginRepository = _UserLoginRepository;
        check ();
    }

    private void check ()
    {
        ToJson.CreateClass createClass = new ToJson.CreateClass ();
        createClass.put ("class_log" , IsLogin.class.getName ());

        if (codeLogin.equals (""))
        {
            valid = false;
            answerToClient = AnswerToClient.NotLoggedIn ();
            l.n (codeLogin , null , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("not login") , createClass.toJson ());
            return;
        }

        vCodeLogin = new VCodeLogin ();
        valid = (vCodeLogin.IsValid (userLoginRepository , codeLogin));

        if (!valid)
        {
            answerToClient = AnswerToClient.NotLoggedIn ();
            l.n (codeLogin , null , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("invalid code login") , createClass.toJson ());
        }
        else l.n (codeLogin , null , getVCodeLogin ().getMainAccount () , null , Thread.currentThread ().getStackTrace () , null , createClass.toJson ());
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
