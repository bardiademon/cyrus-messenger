package com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

/**
 * The ceiling of any request and how long it will be blocked
 */

@Entity
@Table (name = "number_of_submit_request")
public final class NumberOfSubmitRequest
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @Enumerated (EnumType.STRING)
    private SubmitRequestType type;

    @Column (name = "number_of_request", nullable = false)
    private int numberOfRequest;

    @Column (name = "number_of_min_to_be_blocked", nullable = false)
    private long numberOfMinToBeBlocked;

    public NumberOfSubmitRequest ()
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

    public SubmitRequestType getType ()
    {
        return type;
    }

    public void setType (SubmitRequestType type)
    {
        this.type = type;
    }

    public int getNumberOfRequest ()
    {
        return numberOfRequest;
    }

    public void setNumberOfRequest (int numberOfRequest)
    {
        this.numberOfRequest = numberOfRequest;
    }

    public long getNumberOfMinToBeBlocked ()
    {
        return numberOfMinToBeBlocked;
    }

    public void setNumberOfMinToBeBlocked (long numberOfMinToBeBlocked)
    {
        this.numberOfMinToBeBlocked = numberOfMinToBeBlocked;
    }
}
