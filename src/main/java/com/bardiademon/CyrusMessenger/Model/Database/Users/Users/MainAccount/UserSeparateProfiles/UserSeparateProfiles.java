package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserGender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table (name = "user_separate_profile")
public final class UserSeparateProfiles
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    @JsonIgnore
    private MainAccount mainAccount;

    @Column (nullable = false)
    private String name;

    private String family;

    private String bio;

    private String email;

    @Column (name = "my_link")
    private String mylink;

    @Column (name = "gender")
    @Enumerated (EnumType.STRING)
    private UserGender gender = UserGender.not_specified;

    @Column (name = "created_at", nullable = false, updatable = false)
    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Transient
    @JsonProperty ("created_at")
    private String createdAtForShowClient;

    @Column (name = "deleted_at", insertable = false)
    @JsonIgnore
    private LocalDateTime deletedAt;

    @JsonIgnore
    private boolean deleted;

    public UserSeparateProfiles ()
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

    public MainAccount getMainAccount ()
    {
        return mainAccount;
    }

    public void setMainAccount (MainAccount mainAccount)
    {
        this.mainAccount = mainAccount;
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

    public String getBio ()
    {
        return bio;
    }

    public void setBio (String bio)
    {
        this.bio = bio;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getMylink ()
    {
        return mylink;
    }

    public void setMylink (String mylink)
    {
        this.mylink = mylink;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getCreatedAtForShowClient ()
    {
        return createdAtForShowClient;
    }

    public void setCreatedAtForShowClient (String createdAtForShowClient)
    {
        this.createdAtForShowClient = createdAtForShowClient;
    }

    public LocalDateTime getDeletedAt ()
    {
        return deletedAt;
    }

    public void setDeletedAt (LocalDateTime deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted ()
    {
        return deleted;
    }

    public void setDeleted (boolean deleted)
    {
        this.deleted = deleted;
    }

    public UserGender getGender ()
    {
        return gender;
    }

    public void setGender (UserGender gender)
    {
        this.gender = gender;
    }
}
