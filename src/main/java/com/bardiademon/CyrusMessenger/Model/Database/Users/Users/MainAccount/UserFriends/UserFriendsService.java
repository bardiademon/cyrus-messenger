package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFriendsService
{
    public final UserFriendsRepository Repository;

    @Autowired
    public UserFriendsService (UserFriendsRepository Repository)
    {
        this.Repository = Repository;
    }

    public UserFriends findValidFriend (MainAccount user , MainAccount friend)
    {
        return Repository.findByMainAccountAndMainAccountFriendAndDeletedFalse (user , friend);
    }

    public UserFriends findFriend (MainAccount user , MainAccount friend , StatusFriends StatusFriends)
    {
        return Repository.findByMainAccountAndMainAccountFriendAndStatus (user , friend , StatusFriends);
    }

    public List <UserFriends> findValidFriend (MainAccount user)
    {
        return Repository.findByMainAccountAndDeletedFalse (user);
    }

    public List <String> requests (long idUser)
    {
        return Repository.getUsernameRequests (idUser);
    }
}
