package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextOptions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTextOptionsRepository extends JpaRepository <QuestionTextOptions, Long>
{
    QuestionTextOptions findByIdAndQuestionTextId (final long optionId , final long questionTextId);

    @Query ("select count(qto.id) from QuestionTextOptions qto where qto.questionText.id = :QUESTION_TEXT_ID and" +
            " qto.id = :OPTION_ID group by qto.index")
    long countOptions ();
}
