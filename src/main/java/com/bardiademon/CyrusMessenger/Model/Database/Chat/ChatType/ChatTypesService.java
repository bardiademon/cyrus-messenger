package com.bardiademon.CyrusMessenger.Model.Database.Chat.ChatType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ChatTypesService
{
    public final ChatTypesRepository Repository;

    @Autowired
    public ChatTypesService (ChatTypesRepository Repository)
    {
        this.Repository = Repository;
    }
}
