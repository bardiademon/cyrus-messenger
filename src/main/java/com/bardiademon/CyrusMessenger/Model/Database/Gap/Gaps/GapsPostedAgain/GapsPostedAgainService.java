package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsPostedAgain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GapsPostedAgainService
{
    public final GapsPostedAgainRepository Repository;

    @Autowired
    public GapsPostedAgainService (GapsPostedAgainRepository Repository)
    {
        this.Repository = Repository;
    }
}
