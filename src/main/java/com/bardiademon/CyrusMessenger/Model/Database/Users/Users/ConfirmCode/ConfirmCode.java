package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.EnumType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Table (name = "confirm_code")
public class ConfirmCode
{

    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @Column (nullable = false)
    private String code;

    @Column (name = "send_code_to", nullable = false)
    private String sendCodeTo;

    @Column (name = "conform_code_for", nullable = false)
    @Enumerated (EnumType.STRING)
    private ConfirmCodeFor confirmCodeFor;

    @Column (name = "time_to_send", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime timeToSendCode;

    @Column (name = "time_to_confirmed", insertable = false)
    private LocalDateTime timeToConfirmed;

    @Column (name = "time_to_be_outdated", nullable = false, updatable = false)
    private LocalDateTime timeToBeOutdated;

    @Column (name = "is_using")
    private boolean using = false;

    private boolean confirmed;

    public ConfirmCode ()
    {
    }

    public Long getId ()
    {
        return id;
    }

    public void setId (Long id)
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

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getSendCodeTo ()
    {
        return sendCodeTo;
    }

    public void setSendCodeTo (String sendCodeTo)
    {
        this.sendCodeTo = sendCodeTo;
    }

    public ConfirmCodeFor getConfirmCodeFor ()
    {
        return confirmCodeFor;
    }

    public void setConfirmCodeFor (ConfirmCodeFor confirmCodeFor)
    {
        this.confirmCodeFor = confirmCodeFor;
    }

    public LocalDateTime getTimeToSendCode ()
    {
        return timeToSendCode;
    }

    public void setTimeToSendCode (LocalDateTime timeToSendCode)
    {
        this.timeToSendCode = timeToSendCode;
    }

    public LocalDateTime getTimeToConfirmed ()
    {
        return timeToConfirmed;
    }

    public void setTimeToConfirmed (LocalDateTime timeToConfirmed)
    {
        this.timeToConfirmed = timeToConfirmed;
    }

    public LocalDateTime getTimeToBeOutdated ()
    {
        return timeToBeOutdated;
    }

    public void setTimeToBeOutdated (LocalDateTime timeToBeOutdated)
    {
        this.timeToBeOutdated = timeToBeOutdated;
    }

    public boolean isUsing ()
    {
        return using;
    }

    public void setUsing (boolean using)
    {
        this.using = using;
    }

    public boolean isConfirmed ()
    {
        return confirmed;
    }

    public void setConfirmed (boolean confirmed)
    {
        this.confirmed = confirmed;
    }
}
