package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor;

import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChat;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileFor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "show_chat_for")
public class ShowChatFor extends ShowProfileFor
{
    public ShowChatFor ()
    {
    }

    public ShowChatFor (SecurityUserProfile securityUserProfile , SecurityUserChat securityUserChat , AccessLevel security , String showJust , String showJustFriends , String showAllExcept)
    {
        super (securityUserProfile , securityUserChat , security , showJust , showJustFriends , showAllExcept);
    }
}
