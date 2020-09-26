package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapRead;

import java.util.List;
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

    public List <GapRead> findUnRead (long to , long from)
    {
        return Repository.findByGapsToUserIdAndGapsFromIdAndReadIsFalse (to , from);
    }
}
