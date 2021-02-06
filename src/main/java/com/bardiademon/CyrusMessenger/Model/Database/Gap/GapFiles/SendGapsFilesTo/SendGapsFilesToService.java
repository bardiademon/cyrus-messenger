package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.SendGapsFilesTo;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
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

    public void send (final MainAccount mainAccount , final GapsFiles gapsFiles)
    {
        final SendGapsFilesTo sendGapsFilesTo = new SendGapsFilesTo ();
        sendGapsFilesTo.setMainAccount (mainAccount);
        sendGapsFilesTo.setGapsFiles (gapsFiles);
        Repository.save (sendGapsFilesTo);
    }

    public List<SendGapsFilesTo> sendTo (final long userId , final String fileCode)
    {
        return Repository.findByMainAccountIdAndGapsFilesCodeAndDeletedFalse (userId , fileCode);
    }
}
