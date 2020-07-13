package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table (name = "user_blocked")
public final class UserBlocked
{

    @Id
    @GeneratedValue
    @JsonIgnore
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    @JsonIgnore
    private MainAccount mainAccount;

    @OneToOne
    @JoinColumn (name = "id_user_blocked", referencedColumnName = "id")
    @JsonIgnore
    private MainAccount mainAccountBlocked;

    @Transient
    @JsonProperty ("id")
    @JsonInclude (JsonInclude.Include.NON_NULL)
    private Long idBlocked = null;

    @Column (name = "blocked_at", updatable = false)
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime blockedAt;

    @Column (name = "updated_at", insertable = false)
    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updatedAt;

    @Column (name = "validity_time")
    @JsonIgnore
    private LocalDateTime validityTime;

    @Transient
    @JsonProperty ("validity_time")
    private String validityTimeToJson;

    @Column (name = "unblocked_at", insertable = false)
    @JsonIgnore
    private LocalDateTime unblockedAt;

    @JsonIgnore
    private boolean unblocked;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private Type type = Type.all;

    public UserBlocked ()
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

    public MainAccount getMainAccountBlocked ()
    {
        return mainAccountBlocked;
    }

    public void setMainAccountBlocked (MainAccount mainAccountBlocked)
    {
        this.mainAccountBlocked = mainAccountBlocked;
    }

    public LocalDateTime getBlockedAt ()
    {
        return blockedAt;
    }

    public void setBlockedAt (LocalDateTime blockedAt)
    {
        this.blockedAt = blockedAt;
    }

    public LocalDateTime getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getValidityTime ()
    {
        return validityTime;
    }

    public void setValidityTime (LocalDateTime validityTime)
    {
        this.validityTime = validityTime;
    }

    public LocalDateTime getUnblockedAt ()
    {
        return unblockedAt;
    }


    public boolean isUnblocked ()
    {
        return unblocked;
    }

    public void setUnblocked (boolean unblocked)
    {
        this.unblocked = unblocked;
    }

    public Type getType ()
    {
        return type;
    }

    public void setType (Type type)
    {
        this.type = type;
    }

    public String getValidityTimeToJson ()
    {
        return validityTimeToJson;
    }

    public void setValidityTimeToJson (String validityTimeToJson)
    {
        this.validityTimeToJson = validityTimeToJson;
    }

    public void setUnblockedAt (LocalDateTime unblockedAt)
    {
        this.unblockedAt = unblockedAt;
    }

    public Long getIdBlocked ()
    {
        return idBlocked;
    }

    public void setIdBlocked (Long idBlocked)
    {
        this.idBlocked = idBlocked;
    }

    public enum Type
    {
//        cns => Can Not Show

        cns_cover, cns_send_message, cns_profile, cns_find_me, cns_find_be_by_phone,
        cns_bio, cns_email, cns_phone, cns_name, cns_family, cns_username, cns_mylink, all, cns_gender
    }


}
