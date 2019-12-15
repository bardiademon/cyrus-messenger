package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin;

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
@Table (name = "user_login")
public class UserLogin
{

    @Id
    @GeneratedValue
    @Column (unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @Column (nullable = false)
    private boolean successful;

    @Column (nullable = false)
    private String ip;

    /**
     * timeTTLI => Time Trying to login
     */
    @Column (name = "time_ttli", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime timeTTLI;

    @Column (name = "time_login")
    private LocalDateTime timeLogin;

    @Column (name = "time_logout")
    private LocalDateTime timeLogout;

    @Column (name = "credit_up")
    private LocalDateTime creditUp;

    @Column (name = "code_login")
    private String codeLogin;

    public UserLogin ()
    {
    }

    public UserLogin (MainAccount mainAccount , boolean successful , String ip , LocalDateTime timeTTLI , LocalDateTime timeLogin , LocalDateTime timeLogout , LocalDateTime creditUp , String codeLogin)
    {
        this.mainAccount = mainAccount;
        this.successful = successful;
        this.ip = ip;
        this.timeTTLI = timeTTLI;
        this.timeLogin = timeLogin;
        this.timeLogout = timeLogout;
        this.creditUp = creditUp;
        this.codeLogin = codeLogin;
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

    public boolean isSuccessful ()
    {
        return successful;
    }

    public void setSuccessful (boolean successful)
    {
        this.successful = successful;
    }

    public String getIp ()
    {
        return ip;
    }

    public void setIp (String ip)
    {
        this.ip = ip;
    }

    public LocalDateTime getTimeTTLI ()
    {
        return timeTTLI;
    }

    public void setTimeTTLI (LocalDateTime timeTTLI)
    {
        this.timeTTLI = timeTTLI;
    }

    public LocalDateTime getTimeLogin ()
    {
        return timeLogin;
    }

    public void setTimeLogin (LocalDateTime timeLogin)
    {
        this.timeLogin = timeLogin;
    }

    public LocalDateTime getTimeLogout ()
    {
        return timeLogout;
    }

    public void setTimeLogout (LocalDateTime timeLogout)
    {
        this.timeLogout = timeLogout;
    }

    public String getCodeLogin ()
    {
        return codeLogin;
    }

    public void setCodeLogin (String codeLogin)
    {
        this.codeLogin = codeLogin;
    }

    public LocalDateTime getCreditUp ()
    {
        return creditUp;
    }

    public void setCreditUp (LocalDateTime creditUp)
    {
        this.creditUp = creditUp;
    }
}
