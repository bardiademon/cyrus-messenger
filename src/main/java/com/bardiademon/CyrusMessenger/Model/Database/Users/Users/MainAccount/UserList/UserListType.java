package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList;

public enum UserListType
{
    unreliable, trustworthy, family;

    public static UserListType to (String add)
    {
        try
        {
            return valueOf (add);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
