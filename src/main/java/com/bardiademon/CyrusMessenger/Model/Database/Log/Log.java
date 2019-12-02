package com.bardiademon.CyrusMessenger.Model.Database.Log;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Table (name = "log")
public class Log
{

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false, length = 1000)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @Column (updatable = false)
    @CreationTimestamp
    private LocalDateTime time;

    @Column (nullable = false)
    private boolean error;

    @Column (name = "message_error")
    private String messageError;

    @Column (name = "answer_to_client", nullable = false)
    private String answerToClient;

    @Column (nullable = false)
    private String request;

    private String description;

    @Column (nullable = false)
    private String ip;

    public Log ()
    {
    }

    public Log (MainAccount mainAccount , LocalDateTime time , boolean error , String messageError , String answerToClient , String request , String description , String ip)
    {
        this.mainAccount = mainAccount;
        this.time = time;
        this.error = error;
        this.messageError = messageError;
        this.answerToClient = answerToClient;
        this.request = request;
        this.description = description;
        this.ip = ip;
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

    public LocalDateTime getTime ()
    {
        return time;
    }

    public void setTime (LocalDateTime time)
    {
        this.time = time;
    }

    public boolean isError ()
    {
        return error;
    }

    public void setError (boolean error)
    {
        this.error = error;
    }

    public String getMessageError ()
    {
        return messageError;
    }

    public void setMessageError (String messageError)
    {
        this.messageError = messageError;
    }

    public String getAnswerToClient ()
    {
        return answerToClient;
    }

    public void setAnswerToClient (String answerToClient)
    {
        this.answerToClient = answerToClient;
    }

    public String getRequest ()
    {
        return request;
    }

    public void setRequest (String request)
    {
        this.request = request;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getIp ()
    {
        return ip;
    }

    public void setIp (String ip)
    {
        this.ip = ip;
    }
}
