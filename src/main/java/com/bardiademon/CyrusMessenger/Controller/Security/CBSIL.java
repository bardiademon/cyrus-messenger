package com.bardiademon.CyrusMessenger.Controller.Security;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedFor;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// CBSIL => CBS : CheckBlockSystem , IL: IsLogin
public final class CBSIL
{

    @Nullable
    private final IsLogin isLogin;

    @Nullable
    private final AnswerToClient answerToClient;

    private final boolean ok;

    private CBSIL (@Nullable IsLogin _IsLogin , @Nullable AnswerToClient _AnswerToClient , boolean OK)
    {
        this.isLogin = _IsLogin;
        this.answerToClient = _AnswerToClient;
        this.ok = OK;
    }

    public static CheckBlockSystem BSubmitRequest (long IdUser , SubmitRequestType type)
    {
        return new CheckBlockSystem (IdUser , BlockedFor.submit_request , type.name ());
    }

    public static CheckBlockSystem BSubmitRequest (HttpServletRequest Req , SubmitRequestType Type)
    {
        return new CheckBlockSystem (Req , BlockedFor.submit_request , Type.name ());
    }

    public static IsLogin isLogin (String CodeLogin)
    {
        return new IsLogin (CodeLogin);
    }

    public static IsLogin isLogin (String CodeLogin , UserLoginService _UserLoginService)
    {
        return new IsLogin (CodeLogin , _UserLoginService.Repository);
    }

    public static CBSIL Both
            (Object request ,
             HttpServletRequest Req , HttpServletResponse Res ,
             String CodeLogin ,
             UserLoginService _UserLoginService ,
             String Router,SubmitRequestType type)
    {
        boolean OK = false;
        IsLogin _IsLogin = null;
        AnswerToClient _AnswerToClient = null;
        CheckBlockSystem BlockSystem = CBSIL.BSubmitRequest (Req , type);
        if (!BlockSystem.isBlocked ())
        {
            _IsLogin = CBSIL.isLogin (CodeLogin , _UserLoginService);
            if (_IsLogin.isValid ())
            {
                MainAccount mainAccount = _IsLogin.getVCodeLogin ().getMainAccount ();
                BlockSystem = CBSIL.BSubmitRequest (mainAccount.getId () , type);
                if (!BlockSystem.isBlocked ()) OK = true;
                else
                {
                    _AnswerToClient = BlockSystem.getAnswerToClient ();
                    _AnswerToClient.setReqRes (Req , Res);
                    l.n (ToJson.To (request) , Router , mainAccount , _AnswerToClient , Thread.currentThread ().getStackTrace () , new Exception ("blocked by system") , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , CodeLogin).toJson ());
                }
            }
            else
            {
                _AnswerToClient = _IsLogin.getAnswerToClient ();
                _AnswerToClient.setReqRes (Req , Res);
                l.n (ToJson.To (request) , Router , null , _AnswerToClient , Thread.currentThread ().getStackTrace () , new Exception ("not login") , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , CodeLogin).toJson ());
                r.n (Req.getRemoteAddr () , type , true);
            }
        }
        else
        {
            _AnswerToClient = BlockSystem.getAnswerToClient ();
            _AnswerToClient.setReqRes (Req , Res);
            l.n (ToJson.To (request) , Router , null , _AnswerToClient , Thread.currentThread ().getStackTrace () , new Exception ("blocked by system") , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , CodeLogin).toJson ());
        }
        return new CBSIL (_IsLogin , _AnswerToClient , OK);
    }

    @Nullable
    public IsLogin getIsLogin ()
    {
        return isLogin;
    }

    public boolean isOk ()
    {
        return ok;
    }

    @Nullable
    public AnswerToClient getAnswerToClient ()
    {
        return answerToClient;
    }

}
