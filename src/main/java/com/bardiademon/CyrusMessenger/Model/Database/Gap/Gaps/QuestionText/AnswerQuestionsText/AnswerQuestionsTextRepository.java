package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapTextType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerQuestionsTextRepository extends JpaRepository <AnswerQuestionsText, Long>
{
    List <AnswerQuestionsText> findByMainAccountIdAndQuestionTextId (final long userId , final long questionTextId);

    long countByQuestionTextId (final long questionTextId);


    List <AnswerQuestionsText> findByIdAndYesIsTrueAndType (final long questionTextId , final GapTextType gapTextType);

    List <AnswerQuestionsText> findByIdAndYesIsFalseAndType (final long questionTextId , final GapTextType gapTextType);

    long countByIdAndYesIsTrueAndType (final long questionTextId , final GapTextType gapTextType);

    long countByIdAndYesIsFalseAndType (final long questionTextId , final GapTextType gapTextType);

    @Query ("select count(aqt.id),aqt.option.id from AnswerQuestionsText aqt where aqt.questionText.id = :QUESTION_TEXT_ID group by aqt.option.id")
    List <Object[]> countQuestionTextOptions (@Param ("QUESTION_TEXT_ID") final long questionTextId);

}
