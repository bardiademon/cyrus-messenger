package com.bardiademon.CyrusMessenger.Controller.Rest;

public abstract class RouterName
{
    public static final String RN_MAIN_API = "/api";

    public static abstract class RNChat
    {
        public static final String RN_MAIN_CHAT = RouterName.RN_MAIN_API + "/chat";

        public static abstract class RNCover
        {
            public static final String RN_GET_USER_COVER = RN_MAIN_CHAT + "/cover";
            public static final String RN_UPLOAD_USER_COVER = RN_MAIN_CHAT + "/upload_cover";
        }

        public static abstract class RNInfoUser
        {
            public static final String RN_MAIN_INFO_USER = RN_MAIN_CHAT + "/info_user";

            public static final String RN_MAIN = RN_MAIN_CHAT + "/info_user/get/";


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

            public abstract static class RNUpdate
            {
                public static final String RN_MAIN = RN_MAIN_INFO_USER + "/update";

                public abstract static class RNUpdateSecurity
                {
                    public static final String RN_MAIN = RNUpdate.RN_MAIN + "/security";

                    public static final String RN_UPDATE_SECURITY_PROFILE = RN_MAIN + "/profile";

                }
            }

            public static abstract class RNContacts
            {
                public static final String RN_MAIN = RN_MAIN_INFO_USER + "/contacts";

                public static final String RN_NEW_CONTACT = RN_MAIN + "/new";

                public static final String RN_GET_CONTACT = RN_MAIN + "/get";
            }

            public static abstract class RNBlock
            {
                public static final String RN_MAIN = RN_MAIN_INFO_USER + "/block";

                public static final String RN_NEW_BLOCK = RN_MAIN + "/new";

                public static final String RN_GET_BLOCK = RN_MAIN + "/get";
            }
        }

        public static abstract class RNNewInfoUser
        {
            public static final String RN_MAIN = RN_MAIN_CHAT + "/info_user/new/";

            public static final String RN_GENERAL = RN_MAIN + "/general";

            public static final String RN_NEW_FRIEND = RNNewInfoUser.RN_GENERAL + "/new_friend";

            public static final String RN_NEW_EMAIL = RNNewInfoUser.RN_GENERAL + "/new_email";
        }

        public static abstract class RNOtherUsers
        {
            public static final String RN_MAIN = RN_MAIN_CHAT + "/info_user/other_users/";

            public static final String RN_GENERAL = RN_MAIN + "/general";

            public static final String RN_SHOW_PROFILE = RN_GENERAL + "/show_profile";

            public static final String RN_GET_INFO_PROFILE = RN_GENERAL + "/info_profile";


        }


    }

    public static abstract class RNConfirm
    {
        public static final String RN_MAIN = RN_MAIN_API + "/confirm";

        public static final String RN_CONFIRM_PHONE = RN_MAIN + "/phone";
    }

    public abstract static class RNLogin
    {
        public static final String RN_MAIN = RN_MAIN_API + "/login";

        public static final String RN_IS_VALID_UEP = RN_MAIN + "/is_valid_uep";
    }

    public abstract static class RNRegister
    {
        public static final String RN_MAIN = RN_MAIN_API + "/register";
    }
}
