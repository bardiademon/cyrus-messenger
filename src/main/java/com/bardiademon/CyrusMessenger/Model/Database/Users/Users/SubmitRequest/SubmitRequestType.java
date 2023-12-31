package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest;

public enum SubmitRequestType
{
    get_all_profile_pictures_user,
    get_all_profile_pictures_group, delete_profile_picture_user, delete_profile_picture_group,
    get_one_profile_picture_user, get_one_profile_picture_group, upload_profile_picture_user,
    new_block, unblock, update_block, upload_profile_picture_group,
    list_contact, new_contact, remove_contact, remove_contact_with_phone, update_contact, get_general, get_list_friends,
    get_security_profile, get_security_chat, new_email, new_friend,
    new_general, get_info_profile, req_i_can_show_profile,
    update_security_profile, confirmed_phone, is_valid_uep,
    login, register, create_group, find_groups, join_group, owner_groups, group_members,
    new_manager, remove_admin, change_manager, remove_group_member,

    placement_number_user, placement_number_group,
    update_info_group, change_username_group, change_username_user,
    add_user_list, remove_user_list,
    add_user_separate_profile, get_user_separate_profile, get_one_user_separate_profile, remove_user_separate_profile,
    change_user_separate_profile,
    add_friend, del_friend, approve_friend, list_friends, modify_info_user, check_and_send_code__conform_email,

    create_sticker_group, get_groups_ids, get_info_one_sticker_group, delete_sticker_group,

    add_stickers, get_one_info_sticker, delete_sticker, get_stickers_ids, get_one_sticker,
    create_personal_gap, get_personal_gap, delete_personal_gap,

    new_gaps_files, list_gaps_files, remove_gap_file, get_gap_file, before_download_gap_file, find_user_id,


    socket
}
