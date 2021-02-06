package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapRead.GapRead;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapType.GapTypes;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsPostedAgain.GapsPostedAgain;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGaps;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

@Entity
@Table (name = "gaps")

public final class Gaps
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @Column (name = "gap_text", length = 999999)
    private String text;

    @OneToMany (mappedBy = "gaps")
    @LazyCollection (LazyCollectionOption.FALSE)
    private List <GapTypes> gapTypes;

    @Enumerated (EnumType.STRING)
    @Column (name = "gap_for")
    private GapFor gapFor;

    @OneToOne
    @JoinColumn (name = "gap_from", referencedColumnName = "id")
    @JsonManagedReference
    private MainAccount from;

    // if private gap else is null
    @ManyToOne
    @JoinColumn (name = "gap_to_user", referencedColumnName = "id")
    @JsonManagedReference
    private MainAccount toUser;

    // if group gap else is null
    @ManyToOne
    @JoinColumn (name = "gap_to_group", referencedColumnName = "id")
    @JsonInclude (JsonInclude.Include.NON_NULL)
    private Groups toGroup;

    @Column (name = "deleted_for_from_user")
    @JsonIgnore
    private boolean deletedByFromUser;

    @Column (name = "deleted_for_to_user")
    @JsonIgnore
    private boolean deletedForToUser;

    @Column (name = "deleted_at_from_user", insertable = false)
    @JsonIgnore
    private LocalDateTime deletedAt_FromUser;

    @Column (name = "deleted_at_to_user", insertable = false)
    @JsonIgnore
    private LocalDateTime deletedAt_ToUser;

    @ManyToOne
    @JoinColumn (name = "deleted_both_by", referencedColumnName = "id")
    @JsonIgnore
    private MainAccount deletedBothBy;

    @Column (name = "deleted_both_at")
    @JsonIgnore
    private LocalDateTime deletedBothAt;

    @Column (name = "deleted_both")
    @JsonIgnore
    private boolean deletedBoth;

    @OneToMany (mappedBy = "gaps")
    @JsonIgnore
    private List <GapRead> gapRead;

    @ManyToMany
    @LazyCollection (LazyCollectionOption.FALSE)
    @JoinTable (name = "gaps_files", joinColumns = @JoinColumn (name = "gaps_files", referencedColumnName = "id"))
    @JsonIgnore
    private List <GapsFiles> filesGaps;

    @OneToOne
    @JoinColumn (name = "reply_gap", referencedColumnName = "id")
    private Gaps reply;

    @Column (name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column (name = "send_at", updatable = false, nullable = false)
    private LocalDateTime sendAt;

    /**
     * tedad forward shode haye yek gaps ro migire
     */
    @OneToMany (mappedBy = "gap")
    @Where (clause = "`deleted` = false")
    private List <GapsPostedAgain> gapsPostedAgain;

    /**
     * <p>
     * ye gap khali misazam baraye gap forward shode va id gap asli dakhele class GapsPostedAgain mimone
     * inja faghat id GapsPostedAgain ro mizaram
     * </p>
     * <p>
     * in karo baraye in mikonam ke toye tartib gap haei ke mikham begiram moshkeli pish nayad
     * </p>
     */
    @OneToOne
    @JoinColumn (name = "gaps_posted_again", referencedColumnName = "id")
    private GapsPostedAgain postedAgain;

    /*
     * in mige in gap forward shode hast az ye gap dige
     */
    @ManyToOne
    @JoinColumn (name = "forwarded_id", referencedColumnName = "id")
    private GapsPostedAgain forwarded;

    @Column (name = "id_forwarded")
    private boolean isForwarded;

    @Column (name = "index_gap", nullable = false)
    private long indexGap = 0;

    // For private gap , One user = One user
    @ManyToOne
    @JoinColumn (name = "personal_gap", referencedColumnName = "id")
    private PersonalGaps personalGaps;

    public Gaps ()
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

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public List <GapTypes> getGapTypes ()
    {
        return gapTypes;
    }

    public void setGapTypes (List <GapTypes> gapTypes)
    {
        this.gapTypes = gapTypes;
    }

    public GapFor getGapFor ()
    {
        return gapFor;
    }

    public void setGapFor (GapFor gapFor)
    {
        this.gapFor = gapFor;
    }

    public MainAccount getFrom ()
    {
        return from;
    }

    public void setFrom (MainAccount from)
    {
        this.from = from;
    }

    public MainAccount getToUser ()
    {
        return toUser;
    }

    public void setToUser (MainAccount toUser)
    {
        this.toUser = toUser;
    }

    public Groups getToGroup ()
    {
        return toGroup;
    }

    public void setToGroup (Groups toGroup)
    {
        this.toGroup = toGroup;
    }

    public boolean isDeletedByFromUser ()
    {
        return deletedByFromUser;
    }

    public void setDeletedByFromUser (boolean deletedByFromUser)
    {
        this.deletedByFromUser = deletedByFromUser;
    }

    public boolean isDeletedForToUser ()
    {
        return deletedForToUser;
    }

    public void setDeletedForToUser (boolean deletedForToUser)
    {
        this.deletedForToUser = deletedForToUser;
    }

    public LocalDateTime getDeletedAt_FromUser ()
    {
        return deletedAt_FromUser;
    }

    public void setDeletedAt_FromUser (LocalDateTime deletedAt_FromUser)
    {
        this.deletedAt_FromUser = deletedAt_FromUser;
    }

    public LocalDateTime getDeletedAt_ToUser ()
    {
        return deletedAt_ToUser;
    }

    public void setDeletedAt_ToUser (LocalDateTime deletedAt_ToUser)
    {
        this.deletedAt_ToUser = deletedAt_ToUser;
    }

    public MainAccount getDeletedBothBy ()
    {
        return deletedBothBy;
    }

    public void setDeletedBothBy (MainAccount deletedBothBy)
    {
        this.deletedBothBy = deletedBothBy;
    }

    public LocalDateTime getDeletedBothAt ()
    {
        return deletedBothAt;
    }

    public void setDeletedBothAt (LocalDateTime deletedBothAt)
    {
        this.deletedBothAt = deletedBothAt;
    }

    public boolean isDeletedBoth ()
    {
        return deletedBoth;
    }

    public void setDeletedBoth (boolean deletedBoth)
    {
        this.deletedBoth = deletedBoth;
    }

    public List <GapRead> getGapRead ()
    {
        return gapRead;
    }

    public void setGapRead (List <GapRead> gapRead)
    {
        this.gapRead = gapRead;
    }

    public List <GapsFiles> getFilesGaps ()
    {
        return filesGaps;
    }

    public void setFilesGaps (List <GapsFiles> filesGaps)
    {
        this.filesGaps = filesGaps;
    }

    public Gaps getReply ()
    {
        return reply;
    }

    public void setReply (Gaps reply)
    {
        this.reply = reply;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getSendAt ()
    {
        return sendAt;
    }

    public void setSendAt (LocalDateTime sendAt)
    {
        this.sendAt = sendAt;
    }

    public List <GapsPostedAgain> getGapsPostedAgain ()
    {
        return gapsPostedAgain;
    }

    public void setGapsPostedAgain (List <GapsPostedAgain> gapsPostedAgain)
    {
        this.gapsPostedAgain = gapsPostedAgain;
    }

    public GapsPostedAgain getForwarded ()
    {
        return forwarded;
    }

    public void setForwarded (GapsPostedAgain forwarded)
    {
        this.forwarded = forwarded;
    }

    public boolean isForwarded ()
    {
        return isForwarded;
    }

    public void setForwarded (boolean forwarded)
    {
        isForwarded = forwarded;
    }

    public long getIndexGap ()
    {
        return indexGap;
    }

    public void setIndexGap (long indexGap)
    {
        this.indexGap = indexGap;
    }

    public PersonalGaps getPersonalGaps ()
    {
        return personalGaps;
    }

    public void setPersonalGaps (PersonalGaps personalGaps)
    {
        this.personalGaps = personalGaps;
    }

    public GapsPostedAgain getPostedAgain ()
    {
        return postedAgain;
    }

    public void setPostedAgain (GapsPostedAgain postedAgain)
    {
        this.postedAgain = postedAgain;
    }
}