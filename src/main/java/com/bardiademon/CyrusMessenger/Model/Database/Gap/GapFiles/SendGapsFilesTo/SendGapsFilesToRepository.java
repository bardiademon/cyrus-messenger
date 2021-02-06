package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.SendGapsFilesTo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendGapsFilesToRepository extends JpaRepository <SendGapsFilesTo, Long>
{
    List <SendGapsFilesTo> findByMainAccountIdAndDeletedFalse (final long userId);

    long countByMainAccountIdAndDeletedFalse (final long userId);

    List <SendGapsFilesTo> findByMainAccountIdAndGapsFilesCodeAndDeletedFalse (final long userId , final String fileCode);
}
