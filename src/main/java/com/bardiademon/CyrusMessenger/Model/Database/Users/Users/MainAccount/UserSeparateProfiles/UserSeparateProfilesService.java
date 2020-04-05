package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class UserSeparateProfilesService
{
    public final UserSeparateProfilesRepository Repository;

    @Autowired
    public UserSeparateProfilesService (UserSeparateProfilesRepository Repository)
    {
        this.Repository = Repository;
    }
}
