package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class StickersService
{
    public final StickersRepository Repository;

    @Autowired
    public StickersService (final StickersRepository Repository)
    {
        this.Repository = Repository;
    }

    public Stickers getSticker (long id)
    {
        return Repository.getSticker (id);
    }
}
