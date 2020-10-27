package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapRead.GapRead;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapType.GapTypes;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsPostedAgain.GapsPostedAgain;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGaps;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
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
    private List <GapTypes> gapTypes;

    @Enumerated (EnumType.STRING)
    @Column (name = "gap_for", nullable = false)
    private GapFor gapFor;

    @OneToOne
    @JoinColumn (name = "gap_from", referencedColumnName = "id", nullable = false)
    private MainAccount from;

    // if private gap else is null
    @ManyToOne
    @JoinColumn (name = "gap_to_user", referencedColumnName = "id")
    private MainAccount toUser;

    // if group gap else is null
    @ManyToOne
    @JoinColumn (name = "gap_to_group", referencedColumnName = "id")
    private Groups toGroup;

    @Column (name = "deleted_for_from_user")
    private boolean deletedByFromUser;

    @Column (name = "deleted_for_to_user")
    private boolean deletedForToUser;

    @Column (name = "deleted_at_from_user", insertable = false)
    private LocalDateTime deletedAt_FromUser;

    @Column (name = "deleted_at_to_user", insertable = false)
    private LocalDateTime deletedAt_ToUser;

    @ManyToOne
    @JoinColumn (name = "deleted_both_by", referencedColumnName = "id")
    private MainAccount deletedBothBy;

    @OneToMany (mappedBy = "gaps")
    private List <GapRead> gapRead;

    @OneToMany (mappedBy = "gaps")
    private List <GapFiles> filesGaps;

    @OneToOne
    @JoinColumn (name = "reply_gap", referencedColumnName = "id")
    private Gaps reply;

    @Column (name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column (name = "send_at", updatable = false, nullable = false)
    private LocalDateTime sendAt;

    @OneToMany (mappedBy = "gap")
    @Where (clause = "`deleted` = false")
    private List <GapsPostedAgain> gapsPostedAgain;

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

    public List <GapRead> getGapRead ()
    {
        return gapRead;
    }

    public void setGapRead (List <GapRead> gapRead)
    {
        this.gapRead = gapRead;
    }

    public List <GapFiles> getFilesGaps ()
    {
        return filesGaps;
    }

    public void setFilesGaps (List <GapFiles> filesGaps)
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
}