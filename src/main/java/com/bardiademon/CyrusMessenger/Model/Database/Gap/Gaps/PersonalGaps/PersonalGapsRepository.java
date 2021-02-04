package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalGapsRepository extends JpaRepository <PersonalGaps, Long>
{

    @Query ("select pg from PersonalGaps pg where pg.id = :ID and ((pg.createdBy.id = :USER_ID and pg.deletedByCreatedBy = false)" +
            "or (pg.gapWith.id = :USER_ID and pg.deletedForGapWith = false))")
    PersonalGaps byId (@Param ("USER_ID") long userId , @Param ("ID") long id);

    @Query ("select pg from PersonalGaps pg where ((pg.createdBy.id = :USER_ID and pg.deletedByCreatedBy = false) or (pg.gapWith.id = :USER_ID and pg.deletedForGapWith = false)) order by pg.lastMessage desc")
    List <PersonalGaps> personalGaps (@Param ("USER_ID") long userId);

    @Query ("select count(pg) from PersonalGaps pg where ((pg.createdBy.id = :USER_ID and pg.deletedByCreatedBy = false)" +
            "or (pg.gapWith.id = :USER_ID and pg.deletedForGapWith = false)) order by pg.lastMessage desc")
    long countPersonalGaps (@Param ("USER_ID") long userId);

    @Query ("select pg from PersonalGaps pg where ((pg.createdBy.id = :APPLICANT " +
            "and pg.gapWith.id = :USER_ID and pg.deletedByCreatedBy = false)" +
            "or (pg.gapWith.id = :APPLICANT " +
            "and pg.createdBy.id = :USER_ID and pg.deletedForGapWith = false))")
    PersonalGaps personalGaps (@Param ("APPLICANT") long applicant , @Param ("USER_ID") long userId2);

    @Query ("select pg from PersonalGaps pg where ((pg.createdBy.id = :APPLICANT " +
            "and pg.gapWith.id = :USER_ID and pg.deletedByCreatedBy = false)" +
            "or (pg.gapWith.id = :APPLICANT " +
            "and pg.createdBy.id = :USER_ID and pg.deletedForGapWith = false)) and pg.id = :PERSONAL_GAP_ID")
    PersonalGaps personalGaps (@Param ("APPLICANT") long applicant , @Param ("USER_ID") long userId2 , @Param ("PERSONAL_GAP_ID") final long personalGapId);


}
