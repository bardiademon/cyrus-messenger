package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityProfile;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;

@Entity
@Table (name = "group_security_profile")
public class GroupSecurityProfile
{

    private final static int MAX_MEMBER_GROUP = 100000;

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @OneToOne
    @JoinColumn (name = "id_group", referencedColumnName = "id")
    private Groups groups;

    @Column (nullable = false, name = "family_group")
    private boolean familyGroup = false;

    @Column (nullable = false, name = "show_in_search")
    private boolean showInSearch = true;

    @Column (nullable = false, name = "show_list_member")
    private boolean showListMember = true;

    @Column (nullable = false, name = "show_number_of_member")
    private boolean showNumberOfMember = true;

    private String password;

    @Column (nullable = false, name = "show_admin")
    private boolean showAdmin = true;

    @Column (nullable = false, name = "add_member")
    private boolean addMember = false;

    @Column (name = "max_member")
    private Integer maxMember = MAX_MEMBER_GROUP;

    @Column (name = "can_join_group", nullable = false)
    private boolean canJoinGroup = true;

    @Column (name = "show_owner")
    private boolean showOwner = true;

    @Column (name = "max_upload_profile_picture")
    private int maxUploadProfilePicture = 20;

    public GroupSecurityProfile ()
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

    public Groups getGroups ()
    {
        return groups;
    }

    public void setGroups (Groups groups)
    {
        this.groups = groups;
    }

    public boolean isFamilyGroup ()
    {
        return familyGroup;
    }

    public void setFamilyGroup (boolean familyGroup)
    {
        this.familyGroup = familyGroup;
    }

    public boolean isShowInSearch ()
    {
        return showInSearch;
    }

    public void setShowInSearch (boolean showInSearch)
    {
        this.showInSearch = showInSearch;
    }

    public boolean isShowListMember ()
    {
        return showListMember;
    }

    public void setShowListMember (boolean showListMember)
    {
        this.showListMember = showListMember;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public boolean isShowAdmin ()
    {
        return showAdmin;
    }

    public void setShowAdmin (boolean showAdmin)
    {
        this.showAdmin = showAdmin;
    }

    public boolean isAddMember ()
    {
        return addMember;
    }

    public void setAddMember (boolean addMember)
    {
        this.addMember = addMember;
    }

    public Integer getMaxMember ()
    {
        return maxMember;
    }

    public void setMaxMember (Integer maxMember)
    {
        this.maxMember = maxMember;
    }

    public boolean isCanJoinGroup ()
    {
        return canJoinGroup;
    }

    public void setCanJoinGroup (boolean canJoinGroup)
    {
        this.canJoinGroup = canJoinGroup;
    }

    public boolean isShowOwner ()
    {
        return showOwner;
    }

    public void setShowOwner (boolean showOwner)
    {
        this.showOwner = showOwner;
    }

    public int getMaxUploadProfilePicture ()
    {
        return maxUploadProfilePicture;
    }

    public void setMaxUploadProfilePicture (int maxUploadProfilePicture)
    {
        this.maxUploadProfilePicture = maxUploadProfilePicture;
    }

    public boolean isShowNumberOfMember ()
    {
        return showNumberOfMember;
    }

    public void setShowNumberOfMember (boolean showNumberOfMember)
    {
        this.showNumberOfMember = showNumberOfMember;
    }
}
