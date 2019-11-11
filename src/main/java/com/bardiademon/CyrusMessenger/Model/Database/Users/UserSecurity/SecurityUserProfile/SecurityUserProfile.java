package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile;

import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.time.LocalDateTime;

@Entity
@Table (name = "security_user_profile")
public class SecurityUserProfile
{
    @Id
    @GeneratedValue
    @Column (unique = true)
    private long id;

    @OneToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_cover", nullable = false)
    private AccessLevel showCover;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_seen_message", nullable = false)
    private AccessLevel showSeenMessage;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_last_seen", nullable = false)
    private AccessLevel showLastSeen;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_phone", nullable = false)
    private AccessLevel showPhone;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_username", nullable = false)
    private AccessLevel showUsername;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_bio", nullable = false)
    private AccessLevel showBio;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_mylink", nullable = false)
    private AccessLevel showMyLink;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_personal_information", nullable = false)
    private AccessLevel showPersonalInformation;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_name", nullable = false)
    private AccessLevel showName;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_in_search", nullable = false)
    private AccessLevel showInSearch;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_in_group", nullable = false)
    private AccessLevel showInGroup;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_in_channel", nullable = false)
    private AccessLevel showInChannel;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_profile", nullable = false)
    private AccessLevel showInProfile;

    @Column (name = "updated_at", insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public SecurityUserProfile ()
    {
    }

    public SecurityUserProfile (MainAccount mainAccount , AccessLevel showCover , AccessLevel showSeenMessage , AccessLevel showLastSeen , AccessLevel showPhone , AccessLevel showUsername , AccessLevel showBio , AccessLevel showMyLink , AccessLevel showPersonalInformation , AccessLevel showName , AccessLevel showInSearch , AccessLevel showInGroup , AccessLevel showInChannel , AccessLevel showInProfile , LocalDateTime updatedAt)
    {
        this.mainAccount = mainAccount;
        this.showCover = showCover;
        this.showSeenMessage = showSeenMessage;
        this.showLastSeen = showLastSeen;
        this.showPhone = showPhone;
        this.showUsername = showUsername;
        this.showBio = showBio;
        this.showMyLink = showMyLink;
        this.showPersonalInformation = showPersonalInformation;
        this.showName = showName;
        this.showInSearch = showInSearch;
        this.showInGroup = showInGroup;
        this.showInChannel = showInChannel;
        this.showInProfile = showInProfile;
        this.updatedAt = updatedAt;
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

    public AccessLevel getShowCover ()
    {
        return showCover;
    }

    public void setShowCover (AccessLevel showCover)
    {
        this.showCover = showCover;
    }

    public AccessLevel getShowSeenMessage ()
    {
        return showSeenMessage;
    }

    public void setShowSeenMessage (AccessLevel showSeenMessage)
    {
        this.showSeenMessage = showSeenMessage;
    }

    public AccessLevel getShowLastSeen ()
    {
        return showLastSeen;
    }

    public void setShowLastSeen (AccessLevel showLastSeen)
    {
        this.showLastSeen = showLastSeen;
    }

    public AccessLevel getShowPhone ()
    {
        return showPhone;
    }

    public void setShowPhone (AccessLevel showPhone)
    {
        this.showPhone = showPhone;
    }

    public AccessLevel getShowUsername ()
    {
        return showUsername;
    }

    public void setShowUsername (AccessLevel showUsername)
    {
        this.showUsername = showUsername;
    }

    public AccessLevel getShowBio ()
    {
        return showBio;
    }

    public void setShowBio (AccessLevel showBio)
    {
        this.showBio = showBio;
    }

    public AccessLevel getShowMyLink ()
    {
        return showMyLink;
    }

    public void setShowMyLink (AccessLevel showMyLink)
    {
        this.showMyLink = showMyLink;
    }

    public AccessLevel getShowPersonalInformation ()
    {
        return showPersonalInformation;
    }

    public void setShowPersonalInformation (AccessLevel showPersonalInformation)
    {
        this.showPersonalInformation = showPersonalInformation;
    }

    public AccessLevel getShowName ()
    {
        return showName;
    }

    public void setShowName (AccessLevel showName)
    {
        this.showName = showName;
    }

    public AccessLevel getShowInSearch ()
    {
        return showInSearch;
    }

    public void setShowInSearch (AccessLevel showInSearch)
    {
        this.showInSearch = showInSearch;
    }

    public AccessLevel getShowInGroup ()
    {
        return showInGroup;
    }

    public void setShowInGroup (AccessLevel showInGroup)
    {
        this.showInGroup = showInGroup;
    }

    public AccessLevel getShowInChannel ()
    {
        return showInChannel;
    }

    public void setShowInChannel (AccessLevel showInChannel)
    {
        this.showInChannel = showInChannel;
    }

    public AccessLevel getShowInProfile ()
    {
        return showInProfile;
    }

    public void setShowInProfile (AccessLevel showInProfile)
    {
        this.showInProfile = showInProfile;
    }

    public LocalDateTime getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }
}
