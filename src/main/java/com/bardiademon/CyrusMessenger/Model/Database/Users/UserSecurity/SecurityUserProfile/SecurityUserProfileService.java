package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserProfileService
{

    public final SecurityUserProfileRepository Repository;

    @Autowired
    public SecurityUserProfileService (SecurityUserProfileRepository Repository)
    {
        this.Repository = Repository;
    }
}
