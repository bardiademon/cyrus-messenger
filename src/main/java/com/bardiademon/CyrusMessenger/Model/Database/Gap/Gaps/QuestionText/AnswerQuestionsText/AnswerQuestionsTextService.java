package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class AnswerQuestionsTextService
{
    public final AnswerQuestionsTextRepository Repository;

    @Autowired
    public AnswerQuestionsTextService (final AnswerQuestionsTextRepository Repository)
    {
        this.Repository = Repository;
    }

    public List <AnswerQuestionsText> userAnswers (final long userId , final long questionTextId)
    {
        return Repository.findByMainAccountIdAndQuestionTextId (userId , questionTextId);
    }

    public long limCount (final long questionTextId)
    {
        return Repository.countByQuestionTextId (questionTextId);
    }
}
