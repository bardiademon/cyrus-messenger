package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class QuestionTextService
{
    public final QuestionTextRepository Repository;

    @Autowired
    public QuestionTextService (final QuestionTextRepository Repository)
    {
        this.Repository = Repository;
    }


    public QuestionText byId (final long questionTextId , final long gapId)
    {
        return Repository.findByIdAndGapsId (questionTextId , gapId);
    }

    public QuestionText byId ( final long gapId)
    {
        return Repository.findByGapsId (gapId);
    }
}
