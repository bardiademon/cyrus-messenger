package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCode;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeFor;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table (name = "user_confirmed")
public final class Confirmed
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    /**
     * val =>  phone , email , ...
     */
    private String value;

    @OneToOne
    @JoinColumn (name = "id_confirm_code", referencedColumnName = "id")
    private ConfirmCode confirmCode;

    private String code;

    private boolean active = false;

    @Enumerated (EnumType.STRING)
    @Column (name = "confirmed_for", nullable = false)
    private ConfirmCodeFor confirmedFor;

    @Column (name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Confirmed ()
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

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
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

    public ConfirmCodeFor getConfirmedFor ()
    {
        return confirmedFor;
    }

    public void setConfirmedFor (ConfirmCodeFor confirmedFor)
    {
        this.confirmedFor = confirmedFor;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

}
