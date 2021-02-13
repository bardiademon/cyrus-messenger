package com.bardiademon.CyrusMessenger.Controller.Security;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedFor;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;

// CBSIL => CBS : CheckBlockSystem , IL: IsLogin
public final class CBSIL
{

    @Nullable
    private final IsLogin isLogin;

    private final CheckBlockSystem checkBlockSystem;

    @Nullable
    private final AnswerToClient answerToClient;

    private final boolean ok;

    private CBSIL (@Nullable IsLogin _IsLogin , CheckBlockSystem _CheckBlockSystem , @Nullable AnswerToClient _AnswerToClient , boolean OK)
    {
        this.isLogin = _IsLogin;
        this.checkBlockSystem = _CheckBlockSystem;
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

    public static CBSIL BSubmitRequest (String Request , String Router , HttpServletRequest Req , HttpServletResponse Res , SubmitRequestType Type , String CodeLogin , UserLoginService _UserLoginService)
    {
        CheckBlockSystem blockSystem = new CheckBlockSystem (Req , BlockedFor.submit_request , Type.name ());

        AnswerToClient answerToClient;
        if (blockSystem.isBlocked ())
        {
            answerToClient = blockSystem.getAnswerToClient ();
            answerToClient.setReqRes (Req , Res);
            l.n (Request , Router , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.blocked_by_system.name ()) , null);
            r.n (Req.getRemoteAddr () , Type , true);
            return new CBSIL (null , blockSystem , answerToClient , false);
        }
        else
        {
            IsLogin login = isLogin (CodeLogin , _UserLoginService);
            if (login.isValid ())
            {
                blockSystem = new CheckBlockSystem (login.getVCodeLogin ().getMainAccount ().getId () , BlockedFor.submit_request , Type.name ());
                if (blockSystem.isBlocked ())
                {
                    answerToClient = blockSystem.getAnswerToClient ();
                    answerToClient.setReqRes (Req , Res);

                    l.n (Request , Router , login.getVCodeLogin ().getMainAccount () , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.blocked_by_system.name ()) , null);
                    r.n (login.getVCodeLogin ().getMainAccount () , Type , true);
                    return new CBSIL (login , blockSystem , answerToClient , false);
                }
            }
        }
        l.n (Request , Router , null , null , Thread.currentThread ().getStackTrace () , null , ValAnswer.ok.name ());
        if (Req != null) r.n (Req.getRemoteAddr () , Type , false);
        return new CBSIL (null , null , null , true);
    }

    public static IsLogin isLogin (String CodeLogin)
    {
        return new IsLogin (CodeLogin);
    }

    public static IsLogin isLogin (String CodeLogin , UserLoginService _UserLoginService)
    {
        return new IsLogin (CodeLogin , _UserLoginService.Repository);
    }

    public static CBSIL Both (Object request , String CodeLogin , String Router)
    {
        return Both (request , null , null , CodeLogin , (UserLoginService) This.Services ().Get (UserLoginService.class) , Router , SubmitRequestType.socket);
    }

    public static CBSIL Both
            (Object request ,
             HttpServletRequest Req , HttpServletResponse Res , String CodeLogin , UserLoginService _UserLoginService , String Router , SubmitRequestType type)
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
                    l.n (ToJson.To (request) , Router , mainAccount , _AnswerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.blocked_by_system.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , CodeLogin).toJson ());
                }
            }
            else
            {
                _AnswerToClient = _IsLogin.getAnswerToClient ();
                _AnswerToClient.setReqRes (Req , Res);
                l.n (ToJson.To (request) , Router , null , _AnswerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_login.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , CodeLogin).toJson ());
                if (Req != null) r.n (Req.getRemoteAddr () , type , true);
            }
        }
        else
        {
            _AnswerToClient = BlockSystem.getAnswerToClient ();
            _AnswerToClient.setReqRes (Req , Res);
            l.n (ToJson.To (request) , Router , null , _AnswerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.blocked_by_system.name ()) , ToJson.CreateClass.n (MCookie.KEY_CODE_LOGIN_COOKIE , CodeLogin).toJson ());
        }
        return new CBSIL (_IsLogin , BlockSystem , _AnswerToClient , OK);
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

    public CheckBlockSystem getCheckBlockSystem ()
    {
        return checkBlockSystem;
    }

    @Nullable
    public AnswerToClient getAnswerToClient ()
    {
        return answerToClient;
    }

    private enum ValAnswer
    {
        blocked_by_system, not_login, ok
    }

}
