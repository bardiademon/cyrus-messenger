package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails;

import org.springframework.stereotype.Service;

@Service
public class UserEmailsService
{
    public final UserEmailsRepository Repository;

    public UserEmailsService (UserEmailsRepository Repository)
    {
        this.Repository = Repository;
    }
}
