package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Get.Security.Chat;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChat;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChatService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping (value = RouterName.RNInfoUser.RNSecurity.RN_CHAT, method = RequestMethod.POST)
public class Chat
{
    private final UserLoginService userLoginService;
    private final SecurityUserChatService securityUserChatService;

    @Autowired
    public Chat (UserLoginService _UserLoginService , SecurityUserChatService _SecurityUserProfileService)
    {
        this.userLoginService = _UserLoginService;
        this.securityUserChatService = _SecurityUserProfileService;
    }

    @RequestMapping ({"/" , ""})
    public AnswerToClient getInfoSecurityUserChar
            (@RequestBody RequestChat requestChat ,
             @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;
        CheckLogin checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
        if (checkLogin.isValid ())
        {
            if (requestChat.thereIsAtLeastOneTrue ())
            {
                SecurityUserChat securityUserChat
                        = securityUserChatService.Repository.findByMainAccount (checkLogin.getVCodeLogin ().getMainAccount ());

                answerToClient = AnswerToClient.OK ();
                if (requestChat.secSendEmoji)
                    answerToClient.put (KeyAnswer.sec_send_emoji.name () , securityUserChat.getCanSendEmoji ().name ());

                if (requestChat.isSecSendFileType ())
                    answerToClient.put (KeyAnswer.sec_send_file.name () , securityUserChat.getCanSendFile ().name ());

                if (requestChat.isSecSendGif ())
                    answerToClient.put (KeyAnswer.sec_send_gif.name () , securityUserChat.getCanSendGif ().name ());

                if (requestChat.isSecSendInvitation ())
                    answerToClient.put (KeyAnswer.sec_send_invitation.name () , securityUserChat.getCanSendInvitation ().name ());

                if (requestChat.isSecSendMessage ())
                    answerToClient.put (KeyAnswer.sec_send_message.name () , securityUserChat.getCanSendMessage ().name ());

                if (requestChat.isSecSendLink ())
                    answerToClient.put (KeyAnswer.sec_send_link.name () , securityUserChat.getCanSendLink ().name ());

                if (requestChat.isSecSendFileType ())
                    answerToClient.put (KeyAnswer.sec_send_file_type.name () , securityUserChat.getCanSendFileTypes ());

                if (requestChat.isSecSendNumberOfMessageUnread ())
                    answerToClient.put (KeyAnswer.sec_send_number_of_message_unread.name () , securityUserChat.getCanSendNumberOfMessageUnread ());

                if (requestChat.isSecSendSticker ())
                    answerToClient.put (KeyAnswer.sec_send_sticker.name () , securityUserChat.getCanSendSticker ().name ());

                if (requestChat.isSecSendVoice ())
                    answerToClient.put (KeyAnswer.sec_send_voice.name () , securityUserChat.getCanSendVoice ().name ());

            }
            else answerToClient = AnswerToClient.error400 ();
        }
        else answerToClient = checkLogin.getAnswerToClient ();

        return answerToClient;
    }

    public enum KeyAnswer
    {
        sec_send_emoji, sec_send_file, sec_send_gif, sec_send_invitation, sec_send_link,
        sec_send_message, sec_send_file_type, sec_send_number_of_message_unread,
        sec_send_sticker, sec_send_voice
    }

}
