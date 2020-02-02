package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.time.LocalDateTime;

/**
 * All requests sent to the server will be stored here
 */

@Entity
@Table (name = "submit_request")
public final class SubmitRequest
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    /**
     * Requests that are not logged in
     */
    private String ip;

    @JoinColumn (name = "requested_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime requestedAt;

    @Enumerated ( EnumType.STRING)
    @Column(name = "submit_request_type")
    private SubmitRequestType type;

    // sr => submit_request
    @Column(name = "sr_is_active")
    private boolean active;

    public SubmitRequest ()
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

    public LocalDateTime getRequestedAt ()
    {
        return requestedAt;
    }

    public void setRequestedAt (LocalDateTime requestedAt)
    {
        this.requestedAt = requestedAt;
    }

    public SubmitRequestType getType ()
    {
        return type;
    }

    public void setType (SubmitRequestType type)
    {
        this.type = type;
    }

    public String getIp ()
    {
        return ip;
    }

    public void setIp (String ip)
    {
        this.ip = ip;
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
