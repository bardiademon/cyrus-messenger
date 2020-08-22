package com.bardiademon.CyrusMessenger.Model.Database.Chat.ChatFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ChatFilesService
{
    public final ChatFilesRepository Repository;

    @Autowired
    public ChatFilesService (ChatFilesRepository Repository)
    {
        this.Repository = Repository;
    }
}
