package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGaps;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GapsService
{
    public final GapsRepository Repository;

    @Autowired
    public GapsService (GapsRepository Repository)
    {
        this.Repository = Repository;
    }

    public void deleteFrom (long userId , long personalGapId)
    {
        Repository.deleteAllFrom (userId , personalGapId);
    }

    public void deleteTo (long userId , long personalGapId)
    {
        Repository.deleteAllTo (userId , personalGapId);
    }

    public void deleteBoth (PersonalGaps personalGap , MainAccount deletedBy)
    {
        Repository.deleteAllBoth (personalGap.getCreatedBy ().getId () ,
                personalGap.getGapWith ().getId () ,
                deletedBy , personalGap.getId ());
    }
}
