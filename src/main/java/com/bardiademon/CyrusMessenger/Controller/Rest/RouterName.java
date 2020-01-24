package com.bardiademon.CyrusMessenger.Controller.Rest;

public abstract class RouterName
{
    public static final String RN_MAIN_API = "/api";

    public static abstract class RNInfoUser
    {
        public static final String RN_MAIN = RN_MAIN_API + "/info_user/get/";
        public static final String RN_GENERAL = RN_MAIN + "/general";

        public static abstract class RNSecurity
        {
            public static final String RN_MAIN = RNInfoUser.RN_MAIN + "/security";

            public static final String RN_PROFILE = RN_MAIN + "/profile";
            public static final String RN_CHAT = RN_MAIN + "/chat";
        }

        public static abstract class RNGetListFriends
        {
            public static final String RN_GET_LIST_FRIENDS = RNInfoUser.RN_GENERAL + "/list_friends";
        }
    }

    public static abstract class RNNewInfoUser
    {
        public static final String RN_MAIN = RN_MAIN_API + "/info_user/new/";
        public static final String RN_GENERAL = RN_MAIN + "/general";

        public static final String RN_NEW_FRIEND = RNNewInfoUser.RN_GENERAL + "/new_friend";

        public static final String RN_NEW_EMAIL = RNNewInfoUser.RN_GENERAL + "/new_email";

    }

    public static abstract class RNOtherUsers
    {
        public static final String RN_MAIN = RN_MAIN_API + "/info_user/other_users/";

        public static final String RN_GENERAL = RN_MAIN + "/general";

        public static final String RN_SHOW_PROFILE = RN_GENERAL + "/show_profile";


    }

    public static abstract class RNConfirm
    {
        public static final String RN_MAIN = RN_MAIN_API + "/confirm";

        public static final String RN_CONFIRM_PHONE = RN_MAIN + "/phone";
    }
}
