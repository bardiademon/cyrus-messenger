package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name = "user_contacts")
public class UserContacts
{

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
    private String name;

    @Column (nullable = false)
    private String family;

    private String phone;

    @JsonIgnore
    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    @JsonIgnore
    private LocalDateTime deletedAt;


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

}
