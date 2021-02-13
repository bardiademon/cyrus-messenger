package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTextRepository extends JpaRepository <QuestionText, Long>
{
    QuestionText findByIdAndGapsId (final long questionTextId , final long gapId);
}
