package com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel;

public enum Which
{
    cover, seen_message, last_seen,
    phone, email, username, bio,
    id, mylink, personal_information,
    name, family, in_search, in_group,
    in_channel, profile, list_friends, find_me_by_phone, find_me, gender,

    // gap
    // s => send,sh => show
    s_message, s_sticker, s_emoji, s_video, s_gif, s_file, s_image, s_link, s_voice, s_invitation, sh_is_typing
}
