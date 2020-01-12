package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowChatForService
{
    public final ShowChatForRepository Repository;

    @Autowired
    public ShowChatForService (ShowChatForRepository Repository)
    {
        this.Repository = Repository;
    }
}
