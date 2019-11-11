package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ListFriends;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;

@Entity
@Table (name = "list_friends")
public class ListFriends
{

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @OneToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @Column (name = "ids_friends", nullable = false)
    private String idsFriends;

    @Column (name = "lst_deleted")
    private String lstDeleted;

    public ListFriends ()
    {
    }

    public ListFriends (MainAccount mainAccount , String idsFriends , String lstDeleted)
    {
        this.mainAccount = mainAccount;
        this.idsFriends = idsFriends;
        this.lstDeleted = lstDeleted;
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

    public String getIdsFriends ()
    {
        return idsFriends;
    }

    public void setIdsFriends (String idsFriends)
    {
        this.idsFriends = idsFriends;
    }

    public String getLstDeleted ()
    {
        return lstDeleted;
    }

    public void setLstDeleted (String lstDeleted)
    {
        this.lstDeleted = lstDeleted;
    }
}
