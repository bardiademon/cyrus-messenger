package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.Usernames;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlocked;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContacts;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriends;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table (name = "main_account")
@JsonIdentityInfo (generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MainAccount
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @Column (nullable = false)
    private String name;

    private String family;

    @Column (nullable = false)
    private String phone;

    @OneToMany (mappedBy = "mainAccount")
    @JsonIgnore
    @JsonBackReference
    @Where (clause = "deleted_at != null")
    private List<UserFriends> userFriends;

    @OneToMany (mappedBy = "mainAccount")
    @JsonIgnore
    @JsonBackReference
    @Where (clause = "deleted = false")
    private List<UserContacts> userContacts;

    @OneToMany (mappedBy = "mainAccount")
    @JsonIgnore
    @JsonBackReference
    private List<UserBlocked> userBlocked;

    @OneToMany (mappedBy = "mainAccount")
    @JsonIgnore
    @JsonBackReference
    @Where (clause = "`deleted` = false and `this_pic_for` = 'user'")
    private List<ProfilePictures> profilePictures;

    @JsonIgnore
    @OneToOne (mappedBy = "mainAccount")
    @Where (clause = "`username_for` = 'user' and `deleted` = false")
    @JsonBackReference
    private Usernames username;

    @Column (nullable = false)
    @JsonIgnore
    private String password;

    @Column (unique = true)
    @JsonIgnore
    private String email;

    @Column (name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column (name = "updated_at", insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String cover;

    private String bio;

    @Column (name = "my_link")
    private String myLink;

    private boolean active = true;

    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    @Column (name = "friend_confirmation_method", nullable = false)
    @Enumerated (EnumType.STRING)
    private StatusFriends.ApprovalMethod friendConfirmationMethod = StatusFriends.ApprovalMethod.wait;

    @Column (name = "system_block")
    private boolean systemBlock = false;

    @Column (name = "system_block_at", insertable = false)
    private LocalDateTime systemBlockAt;

    public MainAccount ()
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

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getFamily ()
    {
        return family;
    }

    public void setFamily (String family)
    {
        this.family = family;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public List<UserFriends> getUserFriends ()
    {
        return userFriends;
    }

    public void setUserFriends (List<UserFriends> userFriends)
    {
        this.userFriends = userFriends;
    }

    public List<UserContacts> getUserContacts ()
    {
        return userContacts;
    }

    public void setUserContacts (List<UserContacts> userContacts)
    {
        this.userContacts = userContacts;
    }

    public List<UserBlocked> getUserBlocked ()
    {
        return userBlocked;
    }

    public void setUserBlocked (List<UserBlocked> userBlocked)
    {
        this.userBlocked = userBlocked;
    }

    public Usernames getUsername ()
    {
        return username;
    }

    public void setUsername (Usernames username)
    {
        this.username = username;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
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

    public String getCover ()
    {
        return cover;
    }

    public void setCover (String cover)
    {
        this.cover = cover;
    }

    public String getBio ()
    {
        return bio;
    }

    public void setBio (String bio)
    {
        this.bio = bio;
    }

    public String getMyLink ()
    {
        return myLink;
    }

    public void setMyLink (String myLink)
    {
        this.myLink = myLink;
    }

    public boolean isActive ()
    {
        return active;
    }

    public void setActive (boolean active)
    {
        this.active = active;
    }

    public boolean hasCover ()
    {
        return (cover != null && !cover.equals (""));
    }


    public StatusFriends.ApprovalMethod getFriendConfirmationMethod ()
    {
        return friendConfirmationMethod;
    }

    public void setFriendConfirmationMethod (StatusFriends.ApprovalMethod friendConfirmationMethod)
    {
        this.friendConfirmationMethod = friendConfirmationMethod;
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

    public List<ProfilePictures> getProfilePictures ()
    {
        return profilePictures;
    }

    public void setProfilePictures (List<ProfilePictures> profilePictures)
    {
        this.profilePictures = profilePictures;
    }

    public boolean isSystemBlock ()
    {
        return systemBlock;
    }

    public void setSystemBlock (boolean systemBlock)
    {
        this.systemBlock = systemBlock;
    }

    public LocalDateTime getSystemBlockAt ()
    {
        return systemBlockAt;
    }

    public void setSystemBlockAt (LocalDateTime systemBlockAt)
    {
        this.systemBlockAt = systemBlockAt;
    }
}
