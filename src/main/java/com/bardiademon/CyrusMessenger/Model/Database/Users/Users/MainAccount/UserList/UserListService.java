package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public final class UserListService
{
    public UserListRepository Repository;

    @Autowired
    public UserListService (UserListRepository Repository)
    {
        this.Repository = Repository;
    }

    public void add (MainAccount mainAccount , MainAccount user , UserListType type)
    {
        UserList userList = new UserList ();
        userList.setMainAccount (mainAccount);
        userList.setUser (user);
        userList.setType (type);
        Repository.save (userList);
    }

    public UserList getUserList (long idUser , long idUser2 , UserListType type)
    {
        return Repository.findByMainAccountIdAndUserIdAndTypeAndDeletedFalse (idUser , idUser2 , type);
    }

    public UserList getUserListNot (long idUser , long idUser2 , UserListType type)
    {
        return Repository.findByMainAccountIdAndUserIdAndDeletedFalseAndTypeNot (idUser , idUser2 , type);
    }

    public UserList getUserList (long idUser , long idUser2)
    {
        return Repository.findByMainAccountIdAndUserIdAndDeletedFalse (idUser , idUser2);
    }

    public void remove (UserList userList)
    {
        if (userList != null)
        {
            userList.setDeleted (true);
            userList.setDeletedAt (LocalDateTime.now ());
        }
    }
}
