package com.bardiademon.CyrusMessenger.Model.Database.UploadedFiles;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadedFilesRepository extends JpaRepository <UploadedFiles, Long>
{
    @Transactional
    @Modifying
    @Query ("update UploadedFiles file set file.deleted = true , file.deletedAt = current_timestamp where file.id = :ID and file.uploadedBy.id = :USER_ID")
    int delete (@Param ("ID") long id , @Param ("USER_ID") long userId);

    UploadedFiles findById (long id);
}
