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

    public UserEmails findFor (EmailFor emailFor)
    {
        return Repository.findByEmailForAndDeletedFalseOrConfirmedFalse (emailFor);
    }

    public boolean find ()
    {
        return findFor (EmailFor.usp) != null || findFor (EmailFor.ma) != null;
    }
}
