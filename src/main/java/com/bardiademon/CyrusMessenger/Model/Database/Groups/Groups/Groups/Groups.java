package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagement;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroup;
import com.bardiademon.CyrusMessenger.Model.Database.LinkForJoin.LinkForJoin;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table (name = "users_groups")
public class Groups
{

    @Transient
    public static final int MAX_CREATE_GROUP = 5;

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount owner;

    @OneToMany (mappedBy = "groups")
    @Where (clause = "deleted = false")
    private List<GroupManagement> groupManagements;

    @OneToMany (mappedBy = "groups")
    @Where (clause = "time_leave != null")
    private List<JoinGroup> joinGroups;

    @OneToMany (mappedBy = "groups")
    @Where (clause = "deleted = false")
    private List<ProfilePictures> profilePictures;

    @Column (nullable = false)
    private String name;

    @Column (unique = true)
    private String username;

    private String bio;

    private String link;

    private String description;

    @Column (name = "temporarily_closed")
    private boolean temporarilyClosed;

    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    @Column (name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column (name = "updated_at", insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne
    @Where (clause = "deleted = false")
    @JoinColumn (name = "id_link_for_join", referencedColumnName = "id")
    private LinkForJoin linkForJoin;

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public MainAccount getOwner ()
    {
        return owner;
    }

    public void setOwner (MainAccount owner)
    {
        this.owner = owner;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getBio ()
    {
        return bio;
    }

    public void setBio (String bio)
    {
        this.bio = bio;
    }

    public String getLink ()
    {
        return link;
    }

    public void setLink (String link)
    {
        this.link = link;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public boolean isTemporarilyClosed ()
    {
        return temporarilyClosed;
    }

    public void setTemporarilyClosed (boolean temporarilyClosed)
    {
        this.temporarilyClosed = temporarilyClosed;
    }

    public boolean isDeleted ()
    {
        return deleted;
    }

    public void setDeleted (boolean deleted)
    {
        this.deleted = deleted;
    }

    public LocalDateTime getDeletedAt ()
    {
        return deletedAt;
    }

    public void setDeletedAt (LocalDateTime deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public List<GroupManagement> getGroupManagements ()
    {
        return groupManagements;
    }

    public void setGroupManagements (List<GroupManagement> groupManagements)
    {
        this.groupManagements = groupManagements;
    }

    public LinkForJoin getLinkForJoin ()
    {
        return linkForJoin;
    }

    public void setLinkForJoin (LinkForJoin linkForJoin)
    {
        this.linkForJoin = linkForJoin;
    }

    public List<JoinGroup> getJoinGroups ()
    {
        return joinGroups;
    }

    public void setJoinGroups (List<JoinGroup> joinGroups)
    {
        this.joinGroups = joinGroups;
    }

    public List<ProfilePictures> getProfilePictures ()
    {
        return profilePictures;
    }

    public void setProfilePictures (List<ProfilePictures> profilePictures)
    {
        this.profilePictures = profilePictures;
    }
}
