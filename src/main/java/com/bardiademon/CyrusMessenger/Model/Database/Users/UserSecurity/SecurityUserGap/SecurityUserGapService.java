package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserGap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserGapService
{

    public final SecurityUserGapRepository Repository;

    @Autowired
    public SecurityUserGapService (SecurityUserGapRepository Repository)
    {
        this.Repository = Repository;
    }
}
