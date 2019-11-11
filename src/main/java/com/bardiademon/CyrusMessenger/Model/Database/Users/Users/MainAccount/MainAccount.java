package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;

import java.time.LocalDateTime;

@Entity
@Table (name = "main_account")
public class MainAccount
{

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @OneToOne
    private SecurityUserProfile securityUserProfile;

    @Column (nullable = false)
    private String name;

    private String family;

    @Column (nullable = false)
    private String phone;

    @Column (nullable = false, unique = true)
    private String username;

    @Column (unique = true)
    private String email;

    @Column (name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column (name = "updated_at", insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String cover;

    private String bio;

    @Column (name = "my_link")
    private String myLink;


    public MainAccount ()
    {
    }

    public MainAccount (SecurityUserProfile securityUserProfile , String name , String family , String phone , String username , String email , LocalDateTime createdAt , LocalDateTime updatedAt , String cover , String bio , String myLink)
    {
        this.securityUserProfile = securityUserProfile;
        this.name = name;
        this.family = family;
        this.phone = phone;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cover = cover;
        this.bio = bio;
        this.myLink = myLink;
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

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
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

    public SecurityUserProfile getSecurityUserProfile ()
    {
        return securityUserProfile;
    }

    public void setSecurityUserProfile (SecurityUserProfile securityUserProfile)
    {
        this.securityUserProfile = securityUserProfile;
    }
}
