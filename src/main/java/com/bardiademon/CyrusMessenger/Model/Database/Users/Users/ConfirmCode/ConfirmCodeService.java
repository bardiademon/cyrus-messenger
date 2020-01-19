package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmCodeService
{

    public final ConfirmCodeRepository Repository;

    @Autowired
    public ConfirmCodeService (ConfirmCodeRepository Repository)
    {
        this.Repository = Repository;
    }

    public boolean isExistsCode (String code)
    {
        return (Repository.findByCodeAndConfirmedFalseAndUsingFalse (code) != null);
    }
}
