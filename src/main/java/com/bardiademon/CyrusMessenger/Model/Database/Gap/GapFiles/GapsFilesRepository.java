package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GapsFilesRepository extends JpaRepository <GapsFiles, Long>
{
    GapsFiles findByCodeAndDeletedFalse (String code);

    int countAllByUploadedFilesUploadedByIdAndDeletedFalse (final long userId);

    GapsFiles findByUploadedFilesUploadedByIdAndCodeAndDeletedFalse (final long userId , final String fileCode);
}