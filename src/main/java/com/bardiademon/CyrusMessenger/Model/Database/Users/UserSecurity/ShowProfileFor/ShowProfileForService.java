package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowProfileForService
{
    public final ShowProfileForRepository Repository;

    @Autowired
    public ShowProfileForService (ShowProfileForRepository Repository)
    {
        this.Repository = Repository;
    }
}
