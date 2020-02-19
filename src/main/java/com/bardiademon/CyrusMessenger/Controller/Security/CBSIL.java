package com.bardiademon.CyrusMessenger.Controller.Security;

import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedFor;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;

import javax.servlet.http.HttpServletRequest;

// CBSIL => CBS : CheckBlockSystem , IL: IsLogin
public final class CBSIL
{
    private CBSIL ()
    {
    }

    public static CheckBlockSystem BSubmitRequest (long IdUser , SubmitRequestType type)
    {
        return new CheckBlockSystem (IdUser , BlockedFor.submit_request , type.name ());
    }

    public static CheckBlockSystem BSubmitRequest (HttpServletRequest req , SubmitRequestType type)
    {
        return new CheckBlockSystem (req , BlockedFor.submit_request , type.name ());
    }

    public static IsLogin isLogin (String CodeLogin)
    {
        return new IsLogin (CodeLogin);
    }

    public static IsLogin isLogin (String CodeLogin , UserLoginService _UserLoginService)
    {
        return new IsLogin (CodeLogin , _UserLoginService.Repository);
    }
}
