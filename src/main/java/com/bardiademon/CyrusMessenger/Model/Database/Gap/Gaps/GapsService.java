package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGaps;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.Pagination;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GapsService
{
    public final GapsRepository Repository;
    private final EntityManager entityManager;

    @Autowired
    public GapsService (final GapsRepository Repository , final EntityManager _EntityManager)
    {
        this.Repository = Repository;
        this.entityManager = _EntityManager;
    }

    public void deleteFrom (long userId , long personalGapId)
    {
        Repository.deleteAllFrom (userId , personalGapId);
    }

    public Gaps byId (final long gapId , final long personalGapId , final long userId)
    {
        return Repository.byId (gapId , personalGapId , userId);
    }

    public Gaps byId (final long gapId , final long userId)
    {
        return Repository.byId (gapId , userId);
    }

    public Gaps byId (final long gapId)
    {
        return Repository.byId (gapId);
    }

    public void deleteTo (long userId , long personalGapId)
    {
        Repository.deleteAllTo (userId , personalGapId);
    }

    public void deleteBoth (PersonalGaps personalGap , MainAccount deletedBy)
    {
        Repository.deleteAllBoth (personalGap.getCreatedBy ().getId () ,
                personalGap.getGapWith ().getId () ,
                deletedBy , personalGap.getId ());
    }

    @SuppressWarnings ("unchecked")
    public List <Gaps> getPrivateGaps (final long userId , final PersonalGaps personalGaps , final Pagination.Answer paginationAnswer)
    {
        final GapFor gapFor = GapFor.gprivate;

        return ((List <Gaps>) entityManager.createQuery (
                "select gps from Gaps gps where gps.personalGaps.id = :PERSONAL_GAPS_ID" +
                        " and gps.deletedBoth = false and ((gps.from.id = :USER_ID and gps.deletedByFromUser = false) " +
                        "or (gps.toUser.id = :USER_ID and gps.deletedForToUser = false)) and gps.gapFor = :GAP_FOR order by gps.sendAt desc")

                .setParameter ("USER_ID" , userId)
                .setParameter ("PERSONAL_GAPS_ID" , personalGaps.getId ())
                .setParameter ("GAP_FOR" , gapFor)

                .setFirstResult (paginationAnswer.Start)
                .setMaxResults (paginationAnswer.End)

                .getResultList ());
    }

}
