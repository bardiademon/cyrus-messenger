package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerQuestionsTextRepository extends JpaRepository <AnswerQuestionsText, Long>
{
    List <AnswerQuestionsText> findByMainAccountIdAndQuestionTextId (final long userId , final long questionTextId);

    long countByQuestionTextId (final long questionTextId);
}
