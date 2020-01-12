package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserChatService
{

    public final SecurityUserChatRepository Repository;

    @Autowired
    public SecurityUserChatService (SecurityUserChatRepository Repository)
    {
        this.Repository = Repository;
    }
}
