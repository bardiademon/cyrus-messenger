package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity;

public enum AccessLevel
{
    nobody,
    all,
    all_separately,
    separately,
    all_except,
    just;

    public enum Who
    {
        contact, anonymous,

        // az UserList
        family, friend, trustworthy, unreliable;

        public static Who to (String value)
        {
            try
            {
                return valueOf (value);
            }
            catch (Exception e)
            {
                return null;
            }
        }


    }
}
