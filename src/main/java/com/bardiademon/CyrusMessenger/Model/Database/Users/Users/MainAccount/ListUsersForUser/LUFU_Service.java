package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ListUsersForUser;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LUFU_Service
{

    private final LUFU_Repository Repository;

    @Autowired
    public LUFU_Service (LUFU_Repository Repository)
    {
        this.Repository = Repository;
    }

    public ListUsersForUser findValidUser (MainAccount user , MainAccount userList , UserFor userFor)
    {
        return Repository.findByMainAccountAndMainAccountListAndUserForAndDeletedFalse (user , userList , userFor);
    }

}
