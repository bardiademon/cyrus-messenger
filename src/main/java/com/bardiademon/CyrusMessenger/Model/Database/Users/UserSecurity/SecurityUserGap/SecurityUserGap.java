package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserGap;

import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table (name = "security_user_gap")
public class SecurityUserGap
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @OneToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @Enumerated (EnumType.STRING)
    @Column (name = "can_send_message", nullable = false)
    private AccessLevel canSendMessage = AccessLevel.all;

    @Enumerated (EnumType.STRING)
    @Column (name = "can_send_sticker", nullable = false)
    private AccessLevel canSendSticker = AccessLevel.all;

    @Enumerated (EnumType.STRING)
    @Column (name = "can_send_emoji", nullable = false)
    private AccessLevel canSendEmoji = AccessLevel.all;

    @Enumerated (EnumType.STRING)
    @Column (name = "can_send_gif", nullable = false)
    private AccessLevel canSendGif = AccessLevel.all;

    @Enumerated (EnumType.STRING)
    @Column (name = "can_send_file", nullable = false)
    private AccessLevel canSendFile = AccessLevel.all;

    @Column (name = "can_send_file_types")
    private String canSendFileTypes;

    @Enumerated (EnumType.STRING)
    @Column (name = "can_send_image")
    private AccessLevel canSendImage = AccessLevel.all;

    @Column (name = "can_send_number_of_message_unread")
    private Integer canSendNumberOfMessageUnread;

    @Enumerated (EnumType.STRING)
    @Column (name = "can_send_link")
    private AccessLevel canSendLink = AccessLevel.all;

    @Enumerated (EnumType.STRING)
    @Column (name = "can_send_voice")
    private AccessLevel canSendVoice = AccessLevel.all;

    @Enumerated (EnumType.STRING)
    @Column (name = "can_send_video")
    private AccessLevel canSendVideo = AccessLevel.all;

    @Enumerated (EnumType.STRING)
    @Column (name = "can_send_invitation")
    private AccessLevel canSendInvitation = AccessLevel.all;

    @Column (name = "can_anonymous_send_message")
    private boolean canAnonymousSendMessage = true;

    @Enumerated (EnumType.STRING)
    @Column (name = "can_show_is_typing")
    private AccessLevel canShowIsTyping = AccessLevel.all;

    @Column (name = "updated_at", insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated (EnumType.STRING)
    @Column (name = "can_send_question_text")
    private AccessLevel canSendQuestionText = AccessLevel.all;

    public SecurityUserGap ()
    {
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public MainAccount getMainAccount ()
    {
        return mainAccount;
    }

    public void setMainAccount (MainAccount mainAccount)
    {
        this.mainAccount = mainAccount;
    }

    public AccessLevel getCanSendMessage ()
    {
        return canSendMessage;
    }

    public void setCanSendMessage (AccessLevel canSendMessage)
    {
        this.canSendMessage = canSendMessage;
    }

    public AccessLevel getCanSendSticker ()
    {
        return canSendSticker;
    }

    public void setCanSendSticker (AccessLevel canSendSticker)
    {
        this.canSendSticker = canSendSticker;
    }

    public AccessLevel getCanSendEmoji ()
    {
        return canSendEmoji;
    }

    public void setCanSendEmoji (AccessLevel canSendEmoji)
    {
        this.canSendEmoji = canSendEmoji;
    }

    public AccessLevel getCanSendGif ()
    {
        return canSendGif;
    }

    public void setCanSendGif (AccessLevel canSendGif)
    {
        this.canSendGif = canSendGif;
    }

    public AccessLevel getCanSendFile ()
    {
        return canSendFile;
    }

    public void setCanSendFile (AccessLevel canSendFile)
    {
        this.canSendFile = canSendFile;
    }

    public String getCanSendFileTypes ()
    {
        return canSendFileTypes;
    }

    public void setCanSendFileTypes (String canSendFileTypes)
    {
        this.canSendFileTypes = canSendFileTypes;
    }

    public AccessLevel getCanSendImage ()
    {
        return canSendImage;
    }

    public void setCanSendImage (AccessLevel canSendImage)
    {
        this.canSendImage = canSendImage;
    }

    public Integer getCanSendNumberOfMessageUnread ()
    {
        return canSendNumberOfMessageUnread;
    }

    public void setCanSendNumberOfMessageUnread (Integer canSendNumberOfMessageUnread)
    {
        this.canSendNumberOfMessageUnread = canSendNumberOfMessageUnread;
    }

    public AccessLevel getCanSendLink ()
    {
        return canSendLink;
    }

    public void setCanSendLink (AccessLevel canSendLink)
    {
        this.canSendLink = canSendLink;
    }

    public AccessLevel getCanSendVoice ()
    {
        return canSendVoice;
    }

    public void setCanSendVoice (AccessLevel canSendVoice)
    {
        this.canSendVoice = canSendVoice;
    }

    public AccessLevel getCanSendInvitation ()
    {
        return canSendInvitation;
    }

    public void setCanSendInvitation (AccessLevel canSendInvitation)
    {
        this.canSendInvitation = canSendInvitation;
    }

    public LocalDateTime getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public boolean isCanAnonymousSendMessage ()
    {
        return canAnonymousSendMessage;
    }

    public void setCanAnonymousSendMessage (boolean canAnonymousSendMessage)
    {
        this.canAnonymousSendMessage = canAnonymousSendMessage;
    }

    public AccessLevel getCanSendVideo ()
    {
        return canSendVideo;
    }

    public void setCanSendVideo (AccessLevel canSendVideo)
    {
        this.canSendVideo = canSendVideo;
    }

    public AccessLevel getCanShowIsTyping ()
    {
        return canShowIsTyping;
    }

    public void setCanShowIsTyping (AccessLevel canShowIsTyping)
    {
        this.canShowIsTyping = canShowIsTyping;
    }

    public AccessLevel getCanSendQuestionText ()
    {
        return canSendQuestionText;
    }

    public void setCanSendQuestionText (AccessLevel canSendQuestionText)
    {
        this.canSendQuestionText = canSendQuestionText;
    }
}
