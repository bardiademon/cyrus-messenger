package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ConfirmedPhone;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

@Entity
@Table (name = "confirmed_phone")
public class ConfirmedPhone
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    private String phone;

    @OneToOne
    @JoinColumn (name = "id_confirm_code", referencedColumnName = "id")
    private ConfirmCode confirmCode;

    private String code;

    private boolean active = false;

    public ConfirmedPhone ()
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

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public ConfirmCode getConfirmCode ()
    {
        return confirmCode;
    }

    public void setConfirmCode (ConfirmCode confirmCode)
    {
        this.confirmCode = confirmCode;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public boolean isActive ()
    {
        return active;
    }

    public void setActive (boolean active)
    {
        this.active = active;
    }
}
