package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles;

import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.util.List;
import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class UserSeparateProfilesService
{
    public final UserSeparateProfilesRepository Repository;
    private final EnumTypesService enumTypesService;

    @Autowired
    public UserSeparateProfilesService (UserSeparateProfilesRepository Repository , EnumTypesService _EnumTypesService)
    {
        this.Repository = Repository;
        enumTypesService = _EnumTypesService;
    }


    public UserSeparateProfiles getSeparateProfiles (AccessLevel.Who who , MainAccount mainAccount)
    {
        List <UserSeparateProfiles> separate = Repository.findByMainAccountIdAndDeletedFalse (mainAccount.getId ());
        for (UserSeparateProfiles separateProfiles : separate)
        {
            if (enumTypesService.Repository.findById2AndEnumTypeAndDeletedFalse (separateProfiles.getId () , who.name ()) != null)
                return separateProfiles;
        }
        return null;
    }
}
