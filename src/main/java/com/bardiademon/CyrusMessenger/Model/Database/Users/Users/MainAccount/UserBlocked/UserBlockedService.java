package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBlockedService
{
    public final UserBlockedRepository Repository;

    @Autowired
    public UserBlockedService (UserBlockedRepository Repository)
    {
        this.Repository = Repository;
    }


    public UserBlocked isBlocked (long idUser , long idToCheck)
    {
        if (idUser == idToCheck) return null;

        return Repository.findByMainAccountIdAndMainAccountBlockedIdAndUnblockedFalse (idUser , idToCheck);
    }

    public List<UserBlocked> listBlocked (long idUser)
    {
        return Repository.findByMainAccountIdAndUnblockedFalse (idUser);
    }
}
