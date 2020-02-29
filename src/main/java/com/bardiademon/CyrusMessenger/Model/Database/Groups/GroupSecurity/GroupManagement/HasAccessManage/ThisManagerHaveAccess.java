package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagement;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagementAccessLevel.GroupManagementAccessLevel;

public final class ThisManagerHaveAccess
{

    private GroupManagement groupManagement;
    private AccessLevel accessLevel;

    public ThisManagerHaveAccess (GroupManagement _GroupManagement , AccessLevel _AccessLevel)
    {
        this.groupManagement = _GroupManagement;
        this.accessLevel = _AccessLevel;
    }

    public boolean hasAccess ()
    {
        return hasAccess (accessLevel);
    }

    public boolean hasAccess (AccessLevel _AccessLevel)
    {
        this.accessLevel = _AccessLevel;
        if (groupManagement == null) return false;
        GroupManagementAccessLevel accessLevel = groupManagement.getAccessLevel ();

        switch (this.accessLevel)
        {
            case dismiss_user:
                return accessLevel.isDismissUser ();
            case delete_message_user:
                return accessLevel.isDelMessageUser ();
            case add_admin:
                if (accessLevel.isAddAdmin ()) return hasAccess (AccessLevel.change_management_access_level);
                else return false;
            case del_admin:
                return accessLevel.isDelAdmin ();
            case change_management_access_level:
                return accessLevel.isChangeManagementAccessLevel ();
            case change_name_group:
                return accessLevel.isChangeNameGroup ();
            case change_bio:
                return accessLevel.isChangeBio ();
            case change_link:
                return accessLevel.isChangeLink ();
            case change_description:
                return accessLevel.isChangeDescription ();
            case temporarily_closed:
                return accessLevel.isTemporarilyClosed ();
            case upload_picture:
                return accessLevel.isUploadPicture ();
            case del_picture:
                return accessLevel.isDelPicture ();
            case set_main_picture:
                return accessLevel.isSetMainPicture ();
            case change_picture:
                return accessLevel.isChangePicture ();
            case del_main_pic:
                return accessLevel.isDelMainPic ();
            default:
                return false;
        }
    }

}
