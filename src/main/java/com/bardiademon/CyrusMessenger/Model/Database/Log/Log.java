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

    @Column (name = "info_line", length = 100000)
    private String infoLine;

    @Column (name = "answer_to_client", length = 100000)
    private String answerToClient;

    @Column (length = 100000)
    private String request;

    @Column (length = 100000)
    private String description;

    private String ip;

    private String route;

    @Column (name = "http_servlet_request", length = 100000)
    private String httpServletRequest;

    @Column (name = "http_servlet_response", length = 100000)
    private String httpServletResponse;

    public Log ()
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

    public String getInfoLine ()
    {
        return infoLine;
    }

    public void setInfoLine (String infoLine)
    {
        this.infoLine = infoLine;
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

    public String getRoute ()
    {
        return route;
    }

    public void setRoute (String route)
    {
        this.route = route;
    }

    public String getHttpServletRequest ()
    {
        return httpServletRequest;
    }

    public void setHttpServletRequest (String httpServletRequest)
    {
        this.httpServletRequest = httpServletRequest;
    }

    public String getHttpServletResponse ()
    {
        return httpServletResponse;
    }

    public void setHttpServletResponse (String httpServletResponse)
    {
        this.httpServletResponse = httpServletResponse;
    }
}
