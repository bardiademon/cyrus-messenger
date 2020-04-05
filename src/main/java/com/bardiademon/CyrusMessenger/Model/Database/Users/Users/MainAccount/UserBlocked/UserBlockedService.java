package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked;

import com.bardiademon.CyrusMessenger.bardiademon.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List <UserBlocked> isBlocked (long idUser , long idToCheck)
    {
        if (idUser == idToCheck) return null;

        return Repository.findByMainAccountIdAndMainAccountBlockedIdAndUnblockedFalse (idUser , idToCheck);
    }

    public UserBlocked isBlocked (long idUser , long idToCheck , UserBlocked.Type type)
    {
        if (idUser == idToCheck) return null;
        UserBlocked isBlocked = Repository.findByMainAccountIdAndMainAccountBlockedIdAndUnblockedFalseAndType (idUser , idToCheck , type);
        if (isBlocked == null) return null;
        else
        {
            if (Time.BiggerNow (isBlocked.getValidityTime ()))
            {
                isBlocked.setUnblocked (true);
                isBlocked.setUnblockedAt (LocalDateTime.now ());
                Repository.save (isBlocked);
                return null;
            }
            else return isBlocked;
        }
    }

    public List <UserBlocked> listBlocked (long idUser)
    {
        return Repository.findByMainAccountIdAndUnblockedFalse (idUser);
    }
}
