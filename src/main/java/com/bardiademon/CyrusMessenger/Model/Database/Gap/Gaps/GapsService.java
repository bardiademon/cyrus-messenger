package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps;

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
}
