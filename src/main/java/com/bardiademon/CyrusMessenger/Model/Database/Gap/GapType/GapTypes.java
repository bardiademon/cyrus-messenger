package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapType;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.GapType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "gap_types")
public final class GapTypes
{

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @Column (name = "gap_type", nullable = false)
    @Enumerated (EnumType.STRING)
    private GapType gapType;

    @ManyToOne
    @JoinColumn (name = "id_gap", referencedColumnName = "id")
    private Gaps gaps;

    public GapTypes ()
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

    public GapType getGapType ()
    {
        return gapType;
    }

    public void setGapType (GapType gapType)
    {
        this.gapType = gapType;
    }

    public Gaps getGaps ()
    {
        return gaps;
    }

    public void setGaps (Gaps gaps)
    {
        this.gaps = gaps;
    }
}
