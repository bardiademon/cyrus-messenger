package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import java.time.LocalDateTime;

@Entity
@Table (name = "main_account")
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

    @Column (name = "active_phone", insertable = false)
    private LocalDateTime activePhone;

    @Column (nullable = false, unique = true)
    private String username;

    @Column (nullable = false)
    private String password;

    @Column (unique = true)
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

    @Column (nullable = false)
    @Enumerated (EnumType.STRING)
    private MainAccountStatus status = MainAccountStatus.phone_not_confirmed;

    public MainAccount ()
    {
    }

    public MainAccount (String name , String family , String phone , LocalDateTime activePhone , String username , String password , String email , LocalDateTime createdAt , LocalDateTime updatedAt , String cover , String bio , String myLink , MainAccountStatus status)
    {
        this.name = name;
        this.family = family;
        this.phone = phone;
        this.activePhone = activePhone;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cover = cover;
        this.bio = bio;
        this.myLink = myLink;
        this.status = status;
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

    public LocalDateTime getActivePhone ()
    {
        return activePhone;
    }

    public void setActivePhone (LocalDateTime activePhone)
    {
        this.activePhone = activePhone;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
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

    public MainAccountStatus getStatus ()
    {
        return status;
    }

    public void setStatus (MainAccountStatus status)
    {
        this.status = status;
    }
}
