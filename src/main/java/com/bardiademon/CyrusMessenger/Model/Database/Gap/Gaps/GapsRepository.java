package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GapsRepository extends JpaRepository <Gaps, Long>
{
    Gaps findById (long id);

    @Transactional
    @Modifying
    @Query ("update Gaps g set g.deletedByFromUser = true , g.deletedAt_FromUser = current_timestamp " +
            "where g.from.id = :USER_ID and g.personalGaps.id = :PERSONAL_GAP_ID")
    void deleteAllFrom (@Param ("USER_ID") long userId , @Param ("PERSONAL_GAP_ID") long personalGapId);

    @Transactional
    @Modifying
    @Query ("update Gaps g set g.deletedForToUser = true , g.deletedAt_ToUser = current_timestamp " +
            "where g.toUser.id = :USER_ID and g.personalGaps.id = :PERSONAL_GAP_ID")
    void deleteAllTo (@Param ("USER_ID") long userId , @Param ("PERSONAL_GAP_ID") long personalGapId);

    @Transactional
    @Modifying
    @Query ("update Gaps g set g.deletedForToUser = true , g.deletedAt_ToUser = current_timestamp " +
            ", g.deletedByFromUser = true , g.deletedAt_FromUser = current_timestamp , g.deletedBothBy = :DELETED_BY ," +
            " g.deletedBoth = true , g.deletedBothAt = current_timestamp " +
            " where ((g.toUser.id = :USER_ID and g.toUser.id = :USER_ID_2) or" +
            " (g.toUser.id = :USER_ID_2 and g.toUser.id = :USER_ID)) " +
            "and g.personalGaps.id = :PERSONAL_GAP_ID")
    void deleteAllBoth (@Param ("USER_ID") long userId ,
                        @Param ("USER_ID_2") long userId2 ,
                        @Param ("DELETED_BY") MainAccount deletedBy ,
                        @Param ("PERSONAL_GAP_ID") long personalGapId);
}
