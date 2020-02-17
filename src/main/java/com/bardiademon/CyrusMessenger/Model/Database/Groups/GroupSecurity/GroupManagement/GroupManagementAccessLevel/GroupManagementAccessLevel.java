package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagementAccessLevel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "group_management_access_level")
public final class GroupManagementAccessLevel
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    public long id;

    @Column (name = "dismiss_user", nullable = false)
    private boolean dismissUser;

    @Column (name = "del_user", nullable = false)
    private boolean delUser;

    @Column (name = "delete_message_user", nullable = false)
    private boolean delMessageUser;

    @Column (name = "add_admin", nullable = false)
    private boolean addAdmin;

    @Column (name = "del_admin", nullable = false)
    private boolean delAdmin;

    @Column (name = "change_management_access_level", nullable = false)
    private boolean changeManagementAccessLevel;

    @Column (name = "change_name_group", nullable = false)
    private boolean changeNameGroup;

    @Column (name = "change_bio", nullable = false)
    private boolean changeBio;

    @Column (name = "change_link", nullable = false)
    private boolean changeLink;

    @Column (name = "change_description", nullable = false)
    private boolean changeDescription;

    @Column (name = "temporarily_closed", nullable = false)
    private boolean temporarilyClosed;

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

    public boolean isDismissUser ()
    {
        return dismissUser;
    }

    public void setDismissUser (boolean dismissUser)
    {
        this.dismissUser = dismissUser;
    }

    public boolean isDelUser ()
    {
        return delUser;
    }

    public void setDelUser (boolean delUser)
    {
        this.delUser = delUser;
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
}
