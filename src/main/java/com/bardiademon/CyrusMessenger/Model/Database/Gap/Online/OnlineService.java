package com.bardiademon.CyrusMessenger.Model.Database.Gap.Online;

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
        Repository.disableLast (online.getMainAccount ().getId () , LocalDateTime.now ());

        online.setLast (true);
        online.setOfflineAt (LocalDateTime.now ());
        online.getClient ().disconnect ();
        Repository.save (online);
    }

    public LocalDateTime lastSeen (long idUser)
    {
        Online online = Repository.findByMainAccountIdAndLastTrue (idUser);
        if (online != null)
            return online.getOfflineAt ();
        else return null;
    }
}
