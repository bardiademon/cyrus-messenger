package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@Table (name = "user_contacts")
public class UserContacts
{
    @Transient
    @JsonIgnore
    public static final int MIN_PHONE = 10;

    @Transient
    @JsonIgnore
    public static final int MAX_PHONE = 20;

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    @JsonIgnore
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    @JsonIgnore
    private MainAccount mainAccount;

    @OneToOne
    @JoinColumn (name = "id_user_contact", referencedColumnName = "id")
    @JsonIgnore
    private MainAccount mainAccountContact;

    @Column (nullable = false)
    @JsonInclude (JsonInclude.Include.NON_NULL)
    private String name;

    private String phone;

    @Transient
    @JsonInclude (JsonInclude.Include.NON_NULL)
    @JsonProperty("new_phone")
    private String newPhone;

    @JsonIgnore
    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    @JsonIgnore
    private LocalDateTime deletedAt;

    @JsonInclude (JsonInclude.Include.NON_NULL)
    @Transient
    private String message;

    @Transient
    private boolean successfully = false;

    public UserContacts ()
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

    public MainAccount getMainAccountContact ()
    {
        return mainAccountContact;
    }

    public void setMainAccountContact (MainAccount mainAccountContact)
    {
        this.mainAccountContact = mainAccountContact;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
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

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public boolean isSuccessfully ()
    {
        return successfully;
    }

    public void setSuccessfully (boolean successfully)
    {
        this.successfully = successfully;
    }

    public String getNewPhone ()
    {
        return newPhone;
    }

    public void setNewPhone (String newPhone)
    {
        this.newPhone = newPhone;
    }
}
