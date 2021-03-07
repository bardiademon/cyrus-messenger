package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Block.NewBlock;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlocked;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = Domain.RNGap.RNInfoUser.RNBlock.RN_NEW_BLOCK, method = RequestMethod.POST)
public final class NewBlock
{

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;
    private final UserBlockedService userBlockedService;

    @Autowired
    public NewBlock
            (UserLoginService _UserLoginService , MainAccountService _MainAccountService , UserBlockedService _UserBlockedService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.userBlockedService = _UserBlockedService;
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient newBlock
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestNewBlock request)
    {
        AnswerToClient answerToClient;

        String router = Domain.RNGap.RNInfoUser.RNBlock.RN_NEW_BLOCK;
        SubmitRequestType type = SubmitRequestType.new_block;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request == null || (Str.IsEmpty (request.getUsername ()) && Str.IsEmpty (request.getIdUser ().getIdObj ()))
                    || (request.isBlock () && (request.getExtent () <= 0 || Str.IsEmpty (request.getPlusUpTo ()))))
                answerToClient = AnswerToClient.RequestIsNull ();
            else
            {
                if (Str.IsEmpty (request.getIdUser ().getIdObj ()) || request.getIdUser ().isValid ())
                {
                    IdUsernameMainAccount idUsernameMainAccount = new IdUsernameMainAccount (mainAccountService , request.getIdUser ().getId () , request.getUsername ());
                    if (idUsernameMainAccount.isValid ())
                    {
                        MainAccount username = idUsernameMainAccount.getMainAccount ();
                        if (mainAccount.getId () == username.getId ())
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.you_cannot_block_yourself);
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.you_cannot_block_yourself));
                            r.n (mainAccount , type , true);
                        }
                        else
                        {
                            RequestNewBlock.PlusUpTo plusUpTo = null;

                            if (request.isBlock ())
                                plusUpTo = request.checkPlusToUp (request.getPlusUpTo ());

                            UserBlocked.Type typeBlock = checkType (request.getType ());

                            if (request.isBlock () && plusUpTo == null)
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.plus_up_to_invalid);
                                answerToClient.setReqRes (req , res);
                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.plus_up_to_invalid));
                                r.n (mainAccount , type , true);
                            }
                            else if (typeBlock == null)
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.type_invalid);
                                answerToClient.setReqRes (req , res);
                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.type_invalid));
                                r.n (mainAccount , type , true);
                            }
                            else
                            {
                                UserBlocked blocked
                                        = userBlockedService.isBlocked (mainAccount.getId () , username.getId () , typeBlock);

                                if (request.isBlock ())
                                {
                                    assert plusUpTo != null;

                                    if (blocked != null && Time.BiggerNow (blocked.getValidityTime ()))
                                    {
                                        blocked.setUnblocked (true);
                                        blocked.setUnblockedAt (LocalDateTime.now ());
                                        userBlockedService.Repository.save (blocked);
                                        blocked = null;
                                    }

                                    if (blocked == null)
                                    {
                                        blocked = new UserBlocked ();
                                        blocked.setMainAccount (mainAccount);
                                        blocked.setMainAccountBlocked (username);
                                        blocked.setValidityTime (getValidityTime (plusUpTo , request.getExtent ()));
                                        blocked.setType (typeBlock);
                                        blocked = userBlockedService.Repository.save (blocked);
                                        if (blocked.getId () > 0)
                                        {
                                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.blocked);
                                            answerToClient.setReqRes (req , res);
                                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.blocked.name ());
                                            r.n (mainAccount , type , false);
                                        }
                                        else
                                        {
                                            answerToClient = AnswerToClient.ServerError ();
                                            answerToClient.setReqRes (req , res);
                                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e ("server error"));
                                            r.n (mainAccount , type , true);
                                        }
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.already_blocked);
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.already_blocked));
                                        r.n (mainAccount , type , true);
                                    }
                                }
                                else
                                {
                                    if (blocked == null)
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_blocked);
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.not_blocked));
                                    }
                                    else
                                    {
                                        blocked.setUnblocked (true);
                                        blocked.setUnblockedAt (LocalDateTime.now ());
                                        userBlockedService.Repository.save (blocked);
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.unblocked);
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.unblocked));
                                    }
                                    r.n (mainAccount , type , true);
                                }
                            }
                        }


                    }
                    else
                    {
                        answerToClient = idUsernameMainAccount.getAnswerToClient ();
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (IdUsernameMainAccount.class.getName ()));
                        r.n (mainAccount , type , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid ();
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.id_invalid));
                    r.n (mainAccount , type , true);
                }
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private LocalDateTime getValidityTime (RequestNewBlock.PlusUpTo plusUpTo , int extent)
    {
        LocalDateTime now = LocalDateTime.now ();
        switch (plusUpTo)
        {
            case minutes:
                return now.plusMinutes (extent);
            case hour:
                return now.plusHours (extent);
            case month:
                return now.plusMonths (extent);
            case year:
                return now.plusYears (extent);
            default:
                return now;
        }
    }

    public UserBlocked.Type checkType (String type)
    {
        try
        {
            return UserBlocked.Type.valueOf (type);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private enum ValAnswer
    {
        plus_up_to_invalid,
        you_cannot_block_yourself, blocked, already_blocked, not_blocked, unblocked, type_invalid
    }


}
