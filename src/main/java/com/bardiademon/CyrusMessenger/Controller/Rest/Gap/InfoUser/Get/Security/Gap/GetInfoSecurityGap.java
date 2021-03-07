package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Get.Security.Gap;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserGap.SecurityUserGap;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserGap.SecurityUserGapService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping (value = Domain.RNGap.RNInfoUser.RNSecurity.RN_CHAT, method = RequestMethod.POST)
public class GetInfoSecurityGap
{
    private final UserLoginService userLoginService;
    private final SecurityUserGapService securityUserGapService;

    @Autowired
    public GetInfoSecurityGap (UserLoginService _UserLoginService , SecurityUserGapService _SecurityUserProfileService)
    {
        this.userLoginService = _UserLoginService;
        this.securityUserGapService = _SecurityUserProfileService;
    }

    @RequestMapping ({ "/" , "" })
    public AnswerToClient getInfoSecurityUserChar
            (@RequestBody RequestGetInfoSecurityGap requestGap ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;
        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
        {
            if (requestGap.thereIsAtLeastOneTrue ())
            {
                SecurityUserGap securityUserGap
                        = securityUserGapService.Repository.findByMainAccount (isLogin.getVCodeLogin ().getMainAccount ());

                answerToClient = AnswerToClient.OK ();
                if (requestGap.secSendEmoji)
                    answerToClient.put (KeyAnswer.sec_send_emoji , securityUserGap.getCanSendEmoji ());

                if (requestGap.isSecSendFileType ())
                    answerToClient.put (KeyAnswer.sec_send_file , securityUserGap.getCanSendFile ());

                if (requestGap.isSecSendGif ())
                    answerToClient.put (KeyAnswer.sec_send_gif , securityUserGap.getCanSendGif ());

                if (requestGap.isSecSendInvitation ())
                    answerToClient.put (KeyAnswer.sec_send_invitation , securityUserGap.getCanSendInvitation ());

                if (requestGap.isSecSendMessage ())
                    answerToClient.put (KeyAnswer.sec_send_message , securityUserGap.getCanSendMessage ());

                if (requestGap.isSecSendLink ())
                    answerToClient.put (KeyAnswer.sec_send_link , securityUserGap.getCanSendLink ());

                if (requestGap.isSecSendFileType ())
                    answerToClient.put (KeyAnswer.sec_send_file_type , securityUserGap.getCanSendFileTypes ());

                if (requestGap.isSecSendNumberOfMessageUnread ())
                    answerToClient.put (KeyAnswer.sec_send_number_of_message_unread , securityUserGap.getCanSendNumberOfMessageUnread ());

                if (requestGap.isSecSendSticker ())
                    answerToClient.put (KeyAnswer.sec_send_sticker , securityUserGap.getCanSendSticker ());

                if (requestGap.isSecSendVoice ())
                    answerToClient.put (KeyAnswer.sec_send_voice , securityUserGap.getCanSendVoice ());

                if (requestGap.isSecSendQuestionText ())
                    answerToClient.put (KeyAnswer.sec_send_question_text , securityUserGap.getCanSendQuestionText ());

            }
            else answerToClient = AnswerToClient.BadRequest ();
        }
        else answerToClient = isLogin.getAnswerToClient ();

        return answerToClient;
    }

    public enum KeyAnswer
    {
        sec_send_emoji, sec_send_file, sec_send_gif, sec_send_invitation, sec_send_link,
        sec_send_message, sec_send_file_type, sec_send_number_of_message_unread,
        sec_send_sticker, sec_send_voice, sec_send_question_text
    }

}
