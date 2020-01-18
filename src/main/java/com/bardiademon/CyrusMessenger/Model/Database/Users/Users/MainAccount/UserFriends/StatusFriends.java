package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends;

public enum StatusFriends
{
    friend, rejected, awaiting_approval, deleted;

    public enum ApprovalMethod
    {
        approve_all, just_list, all_except, just_list_family, wait, reject
    }
}
