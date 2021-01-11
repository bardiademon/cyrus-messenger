package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles;

import com.bardiademon.CyrusMessenger.bardiademon.Pagination;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GapsFilesService
{
    public final GapsFilesRepository Repository;
    private final EntityManager entityManager;

    @Autowired
    public GapsFilesService (final GapsFilesRepository Repository ,
                             final EntityManager _EntityManager)
    {
        this.Repository = Repository;
        this.entityManager = _EntityManager;
    }

    public GapsFiles byCode (String code)
    {
        return Repository.findByCodeAndDeletedFalse (code);
    }

    public int countCodes (final long userId)
    {
        return Repository.countAllByUploadedFilesUploadedByIdAndDeletedFalse (userId);
    }

    public GapsFiles byCode (final long userId , final String fileCode)
    {
        return Repository.findByUploadedFilesUploadedByIdAndCodeAndDeletedFalse (userId , fileCode);
    }

    @SuppressWarnings ("unchecked")
    public List <String> getCodes (final long userId , final Pagination.Answer paginationAnswer)
    {
        return ((List <String>) entityManager.createQuery (
                "select gf.code from GapsFiles gf " +
                        "where gf.uploadedFiles.uploadedBy.id = :USER_ID and gf.deleted = false"
        )
                .setParameter ("USER_ID" , userId)

                .setFirstResult (paginationAnswer.Start)
                .setMaxResults (paginationAnswer.End)

                .getResultList ());
    }
}
