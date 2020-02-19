package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagementAccessLevel;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table (name = "group_management_access_level")
public final class GroupManagementAccessLevel
{
    @Id
    @GeneratedValue
    @Column (unique = true)
    public long id;

    @OneToOne
    @JoinColumn (name = "id_group_management", referencedColumnName = "id")
    private GroupManagement groupManagement;

    @Column (name = "dismiss_user")
    private boolean dismissUser;

    @Column (name = "delete_message_user")
    private boolean delMessageUser;

    @Column (name = "add_admin")
    private boolean addAdmin;

    @Column (name = "del_admin")
    private boolean delAdmin;

    @Column (name = "change_management_access_level")
    private boolean changeManagementAccessLevel;

    @Column (name = "change_name_group")
    private boolean changeNameGroup;

    @Column (name = "change_bio")
    private boolean changeBio;

    @Column (name = "change_link")
    private boolean changeLink;

    @Column (name = "change_description")
    private boolean changeDescription;

    @Column (name = "temporarily_closed")
    private boolean temporarilyClosed;

    @Column (name = "upload_picture")
    private boolean uploadPicture;

    @Column (name = "del_picture")
    private boolean delPicture;

    @Column (name = "set_main_picture")
    private boolean setMainPicture;

    @Column (name = "change_picture")
    private boolean changePicture;

    @Column (name = "del_main_pic")
    private boolean delMainPic;

    public GroupManagementAccessLevel ()
    {
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public GroupManagement getGroupManagement ()
    {
        return groupManagement;
    }

    public void setGroupManagement (GroupManagement groupManagement)
    {
        this.groupManagement = groupManagement;
    }

    public boolean isDismissUser ()
    {
        return dismissUser;
    }

    public void setDismissUser (boolean dismissUser)
    {
        this.dismissUser = dismissUser;
    }

    public boolean isDelMessageUser ()
    {
        return delMessageUser;
    }

    public void setDelMessageUser (boolean delMessageUser)
    {
        this.delMessageUser = delMessageUser;
    }

    public boolean isAddAdmin ()
    {
        return addAdmin;
    }

    public void setAddAdmin (boolean addAdmin)
    {
        this.addAdmin = addAdmin;
    }

    public boolean isDelAdmin ()
    {
        return delAdmin;
    }

    public void setDelAdmin (boolean delAdmin)
    {
        this.delAdmin = delAdmin;
    }

    public boolean isChangeManagementAccessLevel ()
    {
        return changeManagementAccessLevel;
    }

    public void setChangeManagementAccessLevel (boolean changeManagementAccessLevel)
    {
        this.changeManagementAccessLevel = changeManagementAccessLevel;
    }

    public boolean isChangeNameGroup ()
    {
        return changeNameGroup;
    }

    public void setChangeNameGroup (boolean changeNameGroup)
    {
        this.changeNameGroup = changeNameGroup;
    }

    public boolean isChangeBio ()
    {
        return changeBio;
    }

    public void setChangeBio (boolean changeBio)
    {
        this.changeBio = changeBio;
    }

    public boolean isChangeLink ()
    {
        return changeLink;
    }

    public void setChangeLink (boolean changeLink)
    {
        this.changeLink = changeLink;
    }

    public boolean isChangeDescription ()
    {
        return changeDescription;
    }

    public void setChangeDescription (boolean changeDescription)
    {
        this.changeDescription = changeDescription;
    }

    public boolean isTemporarilyClosed ()
    {
        return temporarilyClosed;
    }

    public void setTemporarilyClosed (boolean temporarilyClosed)
    {
        this.temporarilyClosed = temporarilyClosed;
    }

    public boolean isUploadPicture ()
    {
        return uploadPicture;
    }

    public void setUploadPicture (boolean uploadPicture)
    {
        this.uploadPicture = uploadPicture;
    }

    public boolean isDelPicture ()
    {
        return delPicture;
    }

    public void setDelPicture (boolean delPicture)
    {
        this.delPicture = delPicture;
    }

    public boolean isSetMainPicture ()
    {
        return setMainPicture;
    }

    public void setSetMainPicture (boolean setMainPicture)
    {
        this.setMainPicture = setMainPicture;
    }

    public boolean isChangePicture ()
    {
        return changePicture;
    }

    public void setChangePicture (boolean changePicture)
    {
        this.changePicture = changePicture;
    }

    public boolean isDelMainPic ()
    {
        return delMainPic;
    }

    public void setDelMainPic (boolean delMainPic)
    {
        this.delMainPic = delMainPic;
    }
}
