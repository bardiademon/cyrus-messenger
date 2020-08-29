package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapRead;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GapReadService
{
    public final GapReadRepository Repository;

    @Autowired
    public GapReadService (GapReadRepository Repository)
    {
        this.Repository = Repository;
    }
}
