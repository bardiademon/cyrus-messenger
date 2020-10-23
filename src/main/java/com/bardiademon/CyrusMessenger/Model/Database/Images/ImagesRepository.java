package com.bardiademon.CyrusMessenger.Model.Database.Images;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository <Images, Long>
{
    @Transactional
    @Modifying
    @Query ("update Images ui set ui.deleted = true , ui.deletedAt = current_timestamp where ui.id = :ID and ui.uploadedBy.id = :USER_ID")
    int delete (@Param ("ID") long id , @Param ("USER_ID") long userId);

    Images findById (long id);
}