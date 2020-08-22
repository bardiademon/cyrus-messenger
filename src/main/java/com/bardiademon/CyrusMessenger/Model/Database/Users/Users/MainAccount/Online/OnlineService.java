package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Online;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class OnlineService
{
    public final OnlineRepository Repository;

    @Autowired
    public OnlineService (OnlineRepository Repository)
    {
        this.Repository = Repository;
    }

    public void setOffline (Online online)
    {
        online.setOfflineAt (LocalDateTime.now ());
        online.getClient ().disconnect ();
        Repository.save (online);
    }
}
