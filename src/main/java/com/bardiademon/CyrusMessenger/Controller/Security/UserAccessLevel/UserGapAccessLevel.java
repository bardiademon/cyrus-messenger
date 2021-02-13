package com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel;

import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserGap.SecurityUserGap;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserGap.SecurityUserGapService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.DesEnumTypes;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlocked;
import com.bardiademon.CyrusMessenger.This;

public final class UserGapAccessLevel extends UserProfileAccessLevel
{
    private SecurityUserGap securityUserGap;

//    public UserGapAccessLevel (MainAccount Applicant)
//    {
//        super (Applicant);
//    }

    public UserGapAccessLevel (MainAccount Applicant , MainAccount User)
    {
        super (Applicant , User);
    }

    public boolean hasAccess (Which which)
    {
        if (this.applicant.getId () == this.user.getId ())
            return true;

        if (super.hasAccess (Which.find_me))
        {
            super.which = which;

            this.securityUserGap = This.GetService (SecurityUserGapService.class).Repository.findByMainAccount (user);
            final AccessLevel accessLevel = getAccessLevel ();

            if (accessLevel != null && accessLevel.equals (AccessLevel.nobody)) return false;

            return (securityUserGap != null && accessLevel != null) && (!super.isBlock () && hasAccess (accessLevel , securityUserGap.isCanAnonymousSendMessage ()));
        }
        else return false;
    }

    private AccessLevel getAccessLevel ()
    {
        switch (super.which)
        {
            case s_message:
                super.userBlockedType = UserBlocked.Type.cns_send_message.name ();
                super.desEnumTypes = DesEnumTypes.sug_send_message.name ();
                return securityUserGap.getCanSendMessage ();
            case s_sticker:
                super.userBlockedType = UserBlocked.Type.cns_sticker.name ();
                super.desEnumTypes = DesEnumTypes.sug_sticker.name ();
                return securityUserGap.getCanSendSticker ();
            case s_emoji:
                super.userBlockedType = UserBlocked.Type.cns_emoji.name ();
                super.desEnumTypes = DesEnumTypes.sug_emoji.name ();
                return securityUserGap.getCanSendEmoji ();
            case s_gif:
                super.userBlockedType = UserBlocked.Type.cns_gif.name ();
                super.desEnumTypes = DesEnumTypes.sug_gif.name ();
                return securityUserGap.getCanSendGif ();
            case s_video:
                super.userBlockedType = UserBlocked.Type.cns_video.name ();
                super.desEnumTypes = DesEnumTypes.sug_video.name ();
                return securityUserGap.getCanSendVideo ();
            case s_file:
                super.userBlockedType = UserBlocked.Type.cns_file.name ();
                super.desEnumTypes = DesEnumTypes.sug_file.name ();
                return securityUserGap.getCanSendFile ();
            case s_image:
                super.userBlockedType = UserBlocked.Type.cns_image.name ();
                super.desEnumTypes = DesEnumTypes.sug_image.name ();
                return securityUserGap.getCanSendImage ();
            case s_link:
                super.userBlockedType = UserBlocked.Type.cns_link.name ();
                super.desEnumTypes = DesEnumTypes.sug_link.name ();
                return securityUserGap.getCanSendLink ();
            case s_voice:
                super.userBlockedType = UserBlocked.Type.cns_voice.name ();
                super.desEnumTypes = DesEnumTypes.sug_voice.name ();
                return securityUserGap.getCanSendVoice ();
            case s_invitation:
                super.userBlockedType = UserBlocked.Type.cns_invitation.name ();
                super.desEnumTypes = DesEnumTypes.sug_invitation.name ();
                return securityUserGap.getCanSendInvitation ();
            case sh_is_typing:
                super.userBlockedType = UserBlocked.Type.all.name ();
                super.desEnumTypes = DesEnumTypes.sug_is_typing.name ();
                return securityUserGap.getCanShowIsTyping ();
            case s_question_text:
                super.userBlockedType = UserBlocked.Type.all.name ();
                super.desEnumTypes = DesEnumTypes.sug_question_text.name ();
                return securityUserGap.getCanSendQuestionText ();
            default:
                desEnumTypes = null;
                userBlockedType = null;
                return null;
        }
    }

    public SecurityUserGap getSecurityUserGap ()
    {
        return securityUserGap;
    }
}
