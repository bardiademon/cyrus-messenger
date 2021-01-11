package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.SendGapsFilesTo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class SendGapsFilesToService
{
    public final SendGapsFilesToRepository Repository;

    @Autowired
    public SendGapsFilesToService (final SendGapsFilesToRepository Repository)
    {
        this.Repository = Repository;
    }

    public List <SendGapsFilesTo> list (final long userId)
    {
        return Repository.findByMainAccountIdAndDeletedFalse (userId);
    }

    public long numberOfSubmissions (final long userId)
    {
        return Repository.countByMainAccountIdAndDeletedFalse (userId);
    }
}
