package com.bardiademon.CyrusMessenger.Model.Database.Gap.Emojis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class EmojisService
{
    public final EmojisRepository Repository;

    @Autowired
    public EmojisService (final EmojisRepository Repository)
    {
        this.Repository = Repository;
    }
}
