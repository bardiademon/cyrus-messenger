package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UsersStatus;

import org.springframework.stereotype.Service;

@Service
public class UsersStatusService
{
    public final UsersStatusRepository Repository;

    public UsersStatusService (UsersStatusRepository Repository)
    {
        this.Repository = Repository;
    }
}
