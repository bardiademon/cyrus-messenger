package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.New.General.NewEmail;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VEmail;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCode;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UsersStatus.Status;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UsersStatus.UsersStatus;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UsersStatus.UsersStatusService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.SendMail.SendConformCode;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = Domain.RNChat.RNNewInfoUser.RN_NEW_EMAIL, method = RequestMethod.POST)
public class NewEmail
{

    private MainAccountService mainAccountService;
    private UserLoginService userLoginService;
    private ConfirmCodeService confirmCodeService;
    private UsersStatusService usersStatusService;

    public NewEmail
            (MainAccountService _MainAccountService ,
             UserLoginService _UserLoginService ,
             ConfirmCodeService _ConfirmCodeService ,
             UsersStatusService _UsersStatusService
            )
    {
        this.mainAccountService = _MainAccountService;
        this.userLoginService = _UserLoginService;
        this.confirmCodeService = _ConfirmCodeService;
        this.usersStatusService = _UsersStatusService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient newEmail
            (HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestParam ("email") String email , @RequestParam (value = "replace", required = false) boolean replace)
    {
        AnswerToClient answerToClient;

        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
        {
            VEmail vEmail = new VEmail (email);
            if (!vEmail.check ())
            {
                answerToClient = AnswerToClient.error400 ();
                answerToClient.put (KeyAnswer.answer.name () , ValAnswer.email_invalid.name ());
            }
            else
            {
                MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();
                ConfirmCode confirmCode = sendCode (email , mainAccount);
                if (confirmCode != null)
                {
                    answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED , false);
                    answerToClient.put (KeyAnswer.answer.name () , ValAnswer.the_code_has_been_sent.name ());
                    answerToClient.put (KeyAnswer.waiting_time.name () , Time.getTime (confirmCode.getTimeToBeOutdated ()));
                }
                else
                {
                    if (!mainAccountService.hasEmail (mainAccount.getId ()) || replace)
                    {
                        if (email.equals (mainAccount.getEmail ()))
                        {
                            answerToClient = AnswerToClient.OK ();
                            answerToClient.put (KeyAnswer.answer.name () , ValAnswer.email_is_repetitive.name ());
                        }
                        else
                        {
                            if (mainAccountService.findEmail (email) == null)
                            {

                                int counter = 0;
                                String code = null;
                                boolean createCode = false;
                                while (++counter <= 10)
                                {
                                    code = Code.CreateCodeOF ();
                                    if (!confirmCodeService.isExistsCode (code))
                                    {
                                        createCode = true;
                                        break;
                                    }
                                }
                                if (createCode)
                                {
                                    assert code != null;

                                    new SendConformCode (email , code);
                                    confirmCode = saveCode (code , mainAccount , email);
                                    answerToClient = AnswerToClient.OK ();
                                    answerToClient.put (KeyAnswer.answer.name () , ValAnswer.send_code.name ());
                                    answerToClient.put (KeyAnswer.id.name () , confirmCode.getId ());
                                }
                                else answerToClient = AnswerToClient.ServerError ();
                            }
                            else
                            {
                                answerToClient = AnswerToClient.error400 ();
                                answerToClient.put (KeyAnswer.answer.name () , ValAnswer.email_exists.name ());
                            }
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OK ();
                        answerToClient.put (KeyAnswer.answer.name () , ValAnswer.you_have_email.name ());
                    }

                }
            }
        }
        else answerToClient = isLogin.getAnswerToClient ();

        answerToClient.setResponse (res);

        return answerToClient;
    }

    private ConfirmCode sendCode (String email , MainAccount mainAccount)
    {
        return (confirmCodeService.Repository.findCode (mainAccount.getId () , email , LocalDateTime.now () , ConfirmCodeFor.email));
    }

    private ConfirmCode saveCode (String code , MainAccount mainAccount , String email)
    {
        ConfirmCode confirmCode = new ConfirmCode ();
        confirmCode.setCode (code);
        confirmCode.setConfirmCodeFor (ConfirmCodeFor.email);
        confirmCode.setMainAccount (mainAccount);
        confirmCode.setSendCodeTo (email);
        confirmCode.setTimeToBeOutdated (LocalDateTime.now ().plusMinutes (5));
        return confirmCodeService.Repository.save (confirmCode);
    }

    @RequestMapping (value = "/confirm_code")
    public AnswerToClient confirmCode
            (HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestParam ("id") int id , @RequestParam ("email") String email , @RequestParam (value = "code") String code)
    {
        AnswerToClient answerToClient = AnswerToClient.error400 ();

        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
        {
            VEmail vEmail = new VEmail (email);
            if (vEmail.check ())
            {
                MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();
                if (id > 0)
                {
                    ConfirmCode confirmCode =
                            confirmCodeService.Repository.findCode
                                    (id , mainAccount.getId () , code , email , LocalDateTime.now ());
                    if (confirmCode != null)
                    {
                        codeIsValid (confirmCode);
                        answerToClient = AnswerToClient.OK ();
                        answerToClient.put (KeyAnswer.answer.name () , ValAnswer.confirmed.name ());
                    }
                    else
                        answerToClient.put (KeyAnswer.answer.name () , ValAnswer.code_invalid.name ());
                }
                else
                    answerToClient.put (KeyAnswer.answer.name () , ValAnswer.id_invalid.name ());
            }
            else
                answerToClient.put (KeyAnswer.answer.name () , ValAnswer.email_invalid.name ());
        }
        else answerToClient = isLogin.getAnswerToClient ();

        answerToClient.setResponse (res);

        return answerToClient;
    }

    private void codeIsValid (ConfirmCode confirmCode)
    {
        MainAccount mainAccount = confirmCode.getMainAccount ();

        mainAccount.setEmail (confirmCode.getSendCodeTo ());

        confirmCode.setUsing (true);
        confirmCode.setConfirmed (true);
        confirmCode.setTimeToConfirmed (LocalDateTime.now ());

        UsersStatus usersStatus = new UsersStatus ();
        usersStatus.setActiveRow (true);
        usersStatus.setConfirmCode (confirmCode);
        usersStatus.setMainAccount (mainAccount);
        usersStatus.setStatus (Status.confirmed_email);

        mainAccountService.Repository.save (mainAccount);
        confirmCodeService.Repository.save (confirmCode);
        usersStatusService.Repository.save (usersStatus);
    }

    private enum ValAnswer
    {
        email_exists, send_code, email_invalid, email_is_repetitive,
        the_code_has_been_sent, id_invalid, confirmed, code_invalid, you_have_email
    }

    private enum KeyAnswer
    {
        answer, id, waiting_time
    }
}