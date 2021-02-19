package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextOptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class QuestionTextOptionsService
{
    public final QuestionTextOptionsRepository Repository;

    @Autowired
    public QuestionTextOptionsService (QuestionTextOptionsRepository Repository)
    {
        this.Repository = Repository;
    }

    public QuestionTextOptions byId (final long questionTextId , final long optionId)
    {
        return Repository.findByIdAndQuestionTextId (optionId , questionTextId);
    }
}
