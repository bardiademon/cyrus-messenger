package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest;

public enum SubmitRequestType
{
    get_all_profile_pictures_user,
    get_all_profile_pictures_group, delete_profile_picture_user, delete_profile_picture_group,
    get_one_profile_picture_user, get_one_profile_picture_group, upload_profile_picture_user, new_block, unblock, update_block, upload_profile_picture_group,
    get_contact, new_contact, update_contact, get_general, get_list_friends,
    get_security_profile, get_security_chat, new_email, new_friend,
    new_general, get_info_other_profile, req_i_can_show_profile,
    update_security_profile, confirmed_phone, is_valid_uep,
    login, register, create_group, find_groups, join_group, owner_groups, group_members,
    new_manager, remove_admin, change_manager, remove_group_member,

    placement_number_user, placement_number_group , update_info_group
}
