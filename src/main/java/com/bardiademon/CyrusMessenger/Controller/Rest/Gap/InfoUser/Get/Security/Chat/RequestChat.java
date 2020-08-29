package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Get.Security.Chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestChat
{
    @JsonProperty ("sec_send_emoji")
    public boolean secSendEmoji;

    @JsonProperty ("sec_send_file")
    public boolean secSendfile;

    @JsonProperty ("sec_send_gif")
    public boolean secSendGif;

    @JsonProperty ("sec_send_invitation")
    public boolean secSendInvitation;

    @JsonProperty ("sec_send_link")
    public boolean secSendLink;

    @JsonProperty ("sec_send_message")
    public boolean secSendMessage;

    @JsonProperty ("sec_send_file_type")
    public boolean secSendFileType;

    @JsonProperty ("sec_send_number_of_message_unread")
    public boolean secSendNumberOfMessageUnread;

    @JsonProperty ("sec_send_sticker")
    public boolean secSendSticker;

    @JsonProperty ("sec_send_voice")
    public boolean secSendVoice;

    public RequestChat ()
    {
    }

    public RequestChat (boolean secSendEmoji , boolean secSendfile , boolean secSendGif , boolean secSendInvitation , boolean secSendLink , boolean secSendMessage , boolean secSendFileType , boolean secSendNumberOfMessageUnread , boolean secSendSticker , boolean secSendVoice)
    {
        this.secSendEmoji = secSendEmoji;
        this.secSendfile = secSendfile;
        this.secSendGif = secSendGif;
        this.secSendInvitation = secSendInvitation;
        this.secSendLink = secSendLink;
        this.secSendMessage = secSendMessage;
        this.secSendFileType = secSendFileType;
        this.secSendNumberOfMessageUnread = secSendNumberOfMessageUnread;
        this.secSendSticker = secSendSticker;
        this.secSendVoice = secSendVoice;
    }

    public boolean isSecSendEmoji ()
    {
        return secSendEmoji;
    }

    public void setSecSendEmoji (boolean secSendEmoji)
    {
        this.secSendEmoji = secSendEmoji;
    }

    public boolean isSecSendfile ()
    {
        return secSendfile;
    }

    public void setSecSendfile (boolean secSendfile)
    {
        this.secSendfile = secSendfile;
    }

    public boolean isSecSendGif ()
    {
        return secSendGif;
    }

    public void setSecSendGif (boolean secSendGif)
    {
        this.secSendGif = secSendGif;
    }

    public boolean isSecSendInvitation ()
    {
        return secSendInvitation;
    }

    public void setSecSendInvitation (boolean secSendInvitation)
    {
        this.secSendInvitation = secSendInvitation;
    }

    public boolean isSecSendLink ()
    {
        return secSendLink;
    }

    public void setSecSendLink (boolean secSendLink)
    {
        this.secSendLink = secSendLink;
    }

    public boolean isSecSendMessage ()
    {
        return secSendMessage;
    }

    public void setSecSendMessage (boolean secSendMessage)
    {
        this.secSendMessage = secSendMessage;
    }

    public boolean isSecSendFileType ()
    {
        return secSendFileType;
    }

    public void setSecSendFileType (boolean secSendFileType)
    {
        this.secSendFileType = secSendFileType;
    }

    public boolean isSecSendNumberOfMessageUnread ()
    {
        return secSendNumberOfMessageUnread;
    }

    public void setSecSendNumberOfMessageUnread (boolean secSendNumberOfMessageUnread)
    {
        this.secSendNumberOfMessageUnread = secSendNumberOfMessageUnread;
    }

    public boolean isSecSendSticker ()
    {
        return secSendSticker;
    }

    public void setSecSendSticker (boolean secSendSticker)
    {
        this.secSendSticker = secSendSticker;
    }

    public boolean isSecSendVoice ()
    {
        return secSendVoice;
    }

    public void setSecSendVoice (boolean secSendVoice)
    {
        this.secSendVoice = secSendVoice;
    }

    public boolean thereIsAtLeastOneTrue ()
    {
        return (
                isSecSendEmoji () ||
                        isSecSendfile () ||
                        isSecSendFileType () ||
                        isSecSendGif () ||
                        isSecSendInvitation () ||
                        isSecSendLink () ||
                        isSecSendMessage () ||
                        isSecSendNumberOfMessageUnread () ||
                        isSecSendSticker () ||
                        isSecSendVoice ()
        );
    }
}
