package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ConfirmedPhone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmedPhoneService
{
    public final ConfirmedPhoneRepository Repository;

    @Autowired
    public ConfirmedPhoneService (ConfirmedPhoneRepository Repository)
    {
        this.Repository = Repository;
    }

    public boolean hasCode (String code)
    {
        return getConfirmedPhoneIsActiveConfirmed (code) != null;
    }

    public ConfirmedPhone getConfirmedPhoneIsActiveConfirmed (String code)
    {
        return Repository.findByCodeAndActiveTrueAndConfirmCodeConfirmedTrue (code);
    }
}
