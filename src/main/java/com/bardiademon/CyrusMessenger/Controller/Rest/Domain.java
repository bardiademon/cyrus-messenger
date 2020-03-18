package com.bardiademon.CyrusMessenger.Controller.Rest;

public abstract class Domain
{
    public static final String DOMAIN = "localhost";
    public static final int PORT = 8080;
    public static final String MAIN_DOMAIN = "http://" + DOMAIN + ":" + PORT;

    public static final String RN_MAIN_API = "/api";

    public static final String DOMAIN_API = MAIN_DOMAIN + RN_MAIN_API;

    public static abstract class RNChat
    {
        public static final String RN_MAIN_CHAT = Domain.RN_MAIN_API + "/chat";

        public static abstract class RNProfilePicture
        {
            private static final String RN_PROFILE_PICTURES = RN_MAIN_CHAT + "/profile_pictures";

            private static final String RN_PROFILE_PICTURES_USER = RN_PROFILE_PICTURES + "/user";
            public static final String RN_PROFILE_PICTURES_UPLOAD_USER = RN_PROFILE_PICTURES_USER + "/upload";
            public static final String RN_PROFILE_PICTURES_GET_ALL_USER = RN_PROFILE_PICTURES_USER + "/get_all";
            public static final String RN_PROFILE_PICTURES_GET_ONE_USER = RN_PROFILE_PICTURES_USER + "/get_one";
            public static final String RN_PROFILE_PICTURES_DELETE_USER = RN_PROFILE_PICTURES_USER + "/delete";

            private static final String RN_PROFILE_PICTURES_GROUP = RN_PROFILE_PICTURES + "/group";
            public static final String RN_PROFILE_PICTURES_UPLOAD_GROUP = RN_PROFILE_PICTURES_GROUP + "/upload";
            public static final String RN_PROFILE_PICTURES_GET_ALL_GROUP = RN_PROFILE_PICTURES_GROUP + "/get_all";
            public static final String RN_PROFILE_PICTURES_GET_ONE_GROUP = RN_PROFILE_PICTURES_GROUP + "/get_one";
            public static final String RN_PROFILE_PICTURES_DELETE_GROUP = RN_PROFILE_PICTURES_GROUP + "/delete";

            // PP => Profile Pictures
            public static final String RN_PP_PLACEMENT_NUMBER_GROUP = RN_PROFILE_PICTURES_GROUP + "/placement_number";
            public static final String RN_PP_PLACEMENT_NUMBER_USER = RN_PROFILE_PICTURES_USER + "/placement_number";


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

        public static abstract class RNGroups
        {

            public static final String RN_GROUPS = RN_MAIN_CHAT + "/groups";
            public static final String RN_CREATE_GROUP = RN_GROUPS + "/create_group";
            public static final String RN_FIND_GROUPS = RN_GROUPS + "/find_group";
            public static final String RN_JOIN_GROUP = RN_GROUPS + "/join";


            public static final String RN_UPDATE_INFO_GROUP = RN_GROUPS + "/update_info_group";

            public static abstract class Security
            {
                private static final String RN_GROUPS_SECURITY = RN_GROUPS + "/security";
                public static final String RN_SECURITY_GROUP_MEMBERS = RN_GROUPS_SECURITY + "/group_members";

                public static final String RN_SECURITY_MANAGEMENT = RN_GROUPS_SECURITY + "/management";
                public static final String RN_SECURITY_NEW_MANAGER = RN_SECURITY_MANAGEMENT + "/new_manager";
                public static final String RN_SECURITY_SUSPEND_MANAGER = RN_SECURITY_MANAGEMENT + "/suspend_manager";
                public static final String RN_SECURITY_CHANGE_MANAGER_ACCESS_LEVEL = RN_SECURITY_MANAGEMENT + "/change_manager_access_level";
                public static final String RN_SECURITY_FIRED_MEMBER = RN_SECURITY_MANAGEMENT + "/fired_member";
            }
        }

    }

    public static abstract class RNConfirm
    {
        public static final String RN_MAIN = RN_MAIN_API + "/confirm";

        public static final String RN_CONFIRM_PHONE = RN_MAIN + "/phone";
    }

    public abstract static class RNLogin
    {
        public static final String RN_LOGIN = RN_MAIN_API + "/login";

        public static final String RN_LOGOUT = RN_LOGIN + "/logout";

        public static final String RN_IS_VALID_UEP = RN_LOGIN + "/is_valid_uep";
    }

    public abstract static class RNRegister
    {
        public static final String RN_MAIN = RN_MAIN_API + "/register";
    }
}