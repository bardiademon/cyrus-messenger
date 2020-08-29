package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Get.Security.Chat;

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
public class Chat
{
    private final UserLoginService userLoginService;
    private final SecurityUserGapService securityUserGapService;

    @Autowired
    public Chat (UserLoginService _UserLoginService , SecurityUserGapService _SecurityUserProfileService)
    {
        this.userLoginService = _UserLoginService;
        this.securityUserGapService = _SecurityUserProfileService;
    }

    @RequestMapping ({"/" , ""})
    public AnswerToClient getInfoSecurityUserChar
            (@RequestBody RequestChat requestChat ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;
        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
        {
            if (requestChat.thereIsAtLeastOneTrue ())
            {
                SecurityUserGap securityUserGap
                        = securityUserGapService.Repository.findByMainAccount (isLogin.getVCodeLogin ().getMainAccount ());

                answerToClient = AnswerToClient.OK ();
                if (requestChat.secSendEmoji)
                    answerToClient.put (KeyAnswer.sec_send_emoji.name () , securityUserGap.getCanSendEmoji ().name ());

                if (requestChat.isSecSendFileType ())
                    answerToClient.put (KeyAnswer.sec_send_file.name () , securityUserGap.getCanSendFile ().name ());

                if (requestChat.isSecSendGif ())
                    answerToClient.put (KeyAnswer.sec_send_gif.name () , securityUserGap.getCanSendGif ().name ());

                if (requestChat.isSecSendInvitation ())
                    answerToClient.put (KeyAnswer.sec_send_invitation.name () , securityUserGap.getCanSendInvitation ().name ());

                if (requestChat.isSecSendMessage ())
                    answerToClient.put (KeyAnswer.sec_send_message.name () , securityUserGap.getCanSendMessage ().name ());

                if (requestChat.isSecSendLink ())
                    answerToClient.put (KeyAnswer.sec_send_link.name () , securityUserGap.getCanSendLink ().name ());

                if (requestChat.isSecSendFileType ())
                    answerToClient.put (KeyAnswer.sec_send_file_type.name () , securityUserGap.getCanSendFileTypes ());

                if (requestChat.isSecSendNumberOfMessageUnread ())
                    answerToClient.put (KeyAnswer.sec_send_number_of_message_unread.name () , securityUserGap.getCanSendNumberOfMessageUnread ());

                if (requestChat.isSecSendSticker ())
                    answerToClient.put (KeyAnswer.sec_send_sticker.name () , securityUserGap.getCanSendSticker ().name ());

                if (requestChat.isSecSendVoice ())
                    answerToClient.put (KeyAnswer.sec_send_voice.name () , securityUserGap.getCanSendVoice ().name ());

            }
            else answerToClient = AnswerToClient.error400 ();
        }
        else answerToClient = isLogin.getAnswerToClient ();

        return answerToClient;
    }

    public enum KeyAnswer
    {
        sec_send_emoji, sec_send_file, sec_send_gif, sec_send_invitation, sec_send_link,
        sec_send_message, sec_send_file_type, sec_send_number_of_message_unread,
        sec_send_sticker, sec_send_voice
    }

}
