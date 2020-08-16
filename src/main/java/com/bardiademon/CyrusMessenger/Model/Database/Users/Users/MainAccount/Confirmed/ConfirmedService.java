package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed;

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
        return getConfirmedPhoneIsActiveConfirmed (code) != null;
    }

    public Confirmed getConfirmedPhoneIsActiveConfirmed (String code)
    {
        return Repository.findByCodeAndActiveTrueAndConfirmCodeConfirmedTrue (code);
    }
}
