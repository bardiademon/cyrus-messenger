package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile;

import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Table;
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
    private AccessLevel showCover = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_seen_message", nullable = false)
    private AccessLevel showSeenMessage = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_last_seen", nullable = false)
    private AccessLevel showLastSeen = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_phone", nullable = false)
    private AccessLevel showPhone = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_email", nullable = false)
    private AccessLevel showEmail = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_username", nullable = false)
    private AccessLevel showUsername = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_bio", nullable = false)
    private AccessLevel showBio = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_mylink", nullable = false)
    private AccessLevel showMyLink = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_personal_information", nullable = false)
    private AccessLevel showPersonalInformation = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_name", nullable = false)
    private AccessLevel showName = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_family", nullable = false)
    private AccessLevel showFamily = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_in_search", nullable = false)
    private AccessLevel showInSearch = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_in_group", nullable = false)
    private AccessLevel showInGroup = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_in_channel", nullable = false)
    private AccessLevel showInChannel = AccessLevel.all;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "show_profile", nullable = false)
    private AccessLevel showInProfile = AccessLevel.all;

    @Column (name = "updated_at", insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated (value = EnumType.STRING)
    @Column (name = "list_friends", nullable = false)
    private AccessLevel listFriends = AccessLevel.all;

    @Column (name = "max_upload_profile_pictures", nullable = false)
    private int maxUploadProfilePictures = 20;

    public SecurityUserProfile ()
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

    public AccessLevel getShowEmail ()
    {
        return showEmail;
    }

    public void setShowEmail (AccessLevel showEmail)
    {
        this.showEmail = showEmail;
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

    public AccessLevel getShowFamily ()
    {
        return showFamily;
    }

    public void setShowFamily (AccessLevel showFamily)
    {
        this.showFamily = showFamily;
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

    public AccessLevel getListFriends ()
    {
        return listFriends;
    }

    public void setListFriends (AccessLevel listFriends)
    {
        this.listFriends = listFriends;
    }

    public int getMaxUploadProfilePictures ()
    {
        return maxUploadProfilePictures;
    }

    public void setMaxUploadProfilePictures (int maxUploadProfilePictures)
    {
        this.maxUploadProfilePictures = maxUploadProfilePictures;
    }
}
