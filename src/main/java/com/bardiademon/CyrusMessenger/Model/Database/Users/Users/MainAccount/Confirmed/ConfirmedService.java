package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeFor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmedService
{
    public final ConfirmedRepository Repository;

    @Autowired
    public ConfirmedService (ConfirmedRepository Repository)
    {
        this.Repository = Repository;
    }

    public boolean hasCode (String code)
    {
        return getConfirmedIsActiveConfirmed (code) != null;
    }

    public Confirmed getConfirmedIsActiveConfirmed (String code)
    {
        return Repository.findByCodeAndActiveTrueAndConfirmCodeConfirmedTrue (code);
    }

    public Confirmed hasCodeFor (String code , ConfirmCodeFor confirmCodeFor)
    {
        return Repository.findByCodeAndActiveTrueAndConfirmCodeConfirmedTrueAndConfirmedFor (code , confirmCodeFor);
    }

    public Confirmed fromValue (String value)
    {
        return Repository.findByValueAndActiveTrue (value);
    }
}
