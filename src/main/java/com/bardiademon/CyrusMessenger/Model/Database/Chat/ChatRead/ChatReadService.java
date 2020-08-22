package com.bardiademon.CyrusMessenger.Model.Database.Chat.ChatRead;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ChatReadService
{
    public final ChatReadRepository Repository;

    @Autowired
    public ChatReadService (ChatReadRepository Repository)
    {
        this.Repository = Repository;
    }
}
