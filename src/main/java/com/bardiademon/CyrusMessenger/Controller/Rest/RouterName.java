package com.bardiademon.CyrusMessenger.Controller.Rest;

public abstract class RouterName
{
    public static final String RN_MAIN_API = "/api";


    public static abstract class RNInfoUser
    {
        public static final String RN_MAIN = RN_MAIN_API + "/info_user";
        public static final String RN_GENERAL = RN_MAIN + "/general";

        public static abstract class RNSecurity
        {
            public static final String RN_MAIN = RNInfoUser.RN_MAIN + "/security";

            public static final String RN_PROFILE = RN_MAIN + "/profile";
            public static final String RN_CHAT = RN_MAIN + "/chat";
        }
    }
}
