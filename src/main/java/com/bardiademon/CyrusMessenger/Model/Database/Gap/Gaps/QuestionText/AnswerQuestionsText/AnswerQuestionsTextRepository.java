package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapTextType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerQuestionsTextRepository extends JpaRepository <AnswerQuestionsText, Long>
{
    List <AnswerQuestionsText> findByMainAccountIdAndQuestionTextId (final long userId , final long questionTextId);

    AnswerQuestionsText findByMainAccountIdAndQuestionTextIdAndOptionId (final long userId , final long questionTextId , final long optionId);

    long countByQuestionTextId (final long questionTextId);


    List <AnswerQuestionsText> findByIdAndYesIsTrueAndType (final long questionTextId , final GapTextType gapTextType);

    List <AnswerQuestionsText> findByIdAndYesIsFalseAndType (final long questionTextId , final GapTextType gapTextType);

    long countByIdAndYesIsTrueAndType (final long questionTextId , final GapTextType gapTextType);

    long countByIdAndYesIsFalseAndType (final long questionTextId , final GapTextType gapTextType);

    @Query ("select count(aqt.id),aqt.option.id from AnswerQuestionsText aqt where aqt.questionText.id = :QUESTION_TEXT_ID group by aqt.option.id")
    List <Object[]> countQuestionTextOptions (@Param ("QUESTION_TEXT_ID") final long questionTextId);

    @Query ("select aqt.mainAccount,aqt.yes from AnswerQuestionsText aqt where aqt.questionText.id = :QUESTION_TEXT_ID and aqt.type = :TYPE group by aqt.yes")
    List <Object[]> findAnswerYesNo (@Param ("QUESTION_TEXT_ID") final long questionTextId , @Param ("TYPE") final GapTextType type);

    @Query ("select aqt.mainAccount from AnswerQuestionsText aqt where aqt.questionText.id = :QUESTION_TEXT_ID and aqt.type = :TYPE and aqt.option.id = :OPTION_ID")
    List <MainAccount> findAnswerOption (@Param ("QUESTION_TEXT_ID") final long questionTextId , @Param ("TYPE") final GapTextType type , @Param ("OPTION_ID") final long optionId);

}
