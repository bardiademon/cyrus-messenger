package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class PersonalGapsService
{
    public final PersonalGapsRepository Repository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public PersonalGapsService (final PersonalGapsRepository Repository , final EntityManager EntityManager)
    {
        this.Repository = Repository;
        this.entityManager = EntityManager;
    }

    public PersonalGaps byId (long id , long userId)
    {
        return Repository.byId (userId , id);
    }

    public PersonalGaps getPersonalGap (long applicant , long userId)
    {
        return Repository.personalGaps (applicant , userId);
    }

    public PersonalGaps getPersonalGap (final long applicant , final long userId , final long personalGapId)
    {
        return Repository.personalGaps (applicant , userId , personalGapId);
    }

    @SuppressWarnings (value = "unchecked")
    public List <PersonalGaps> getPersonalGaps (long userId , int start , int end)
    {
        return ((List <PersonalGaps>) entityManager
                .createQuery ("select pg from PersonalGaps pg where " +
                        "((pg.createdBy.id = :USER_ID and pg.deletedByCreatedBy = false) or (pg.gapWith.id = :USER_ID and pg.deletedForGapWith = false))" +
                        " order by pg.lastMessage desc")
                .setParameter ("USER_ID" , userId)
                .setFirstResult (start)
                .setMaxResults (end)
                .getResultList ());
    }

    public long getCountPersonalGaps (long userId)
    {
        return Repository.countPersonalGaps (userId);
    }

}
