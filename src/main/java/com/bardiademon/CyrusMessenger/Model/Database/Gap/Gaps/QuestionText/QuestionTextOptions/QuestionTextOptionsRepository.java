package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextOptions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTextOptionsRepository extends JpaRepository <QuestionTextOptions, Long>
{
    QuestionTextOptions findByIdAndQuestionTextId (final long optionId , final long questionTextId);
}
