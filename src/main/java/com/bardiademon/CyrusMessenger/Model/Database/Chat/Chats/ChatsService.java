package com.bardiademon.CyrusMessenger.Model.Database.Chat.Chats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ChatsService
{
    public final ChatsRepository Repository;

    @Autowired
    public ChatsService (ChatsRepository Repository)
    {
        this.Repository = Repository;
    }
}
