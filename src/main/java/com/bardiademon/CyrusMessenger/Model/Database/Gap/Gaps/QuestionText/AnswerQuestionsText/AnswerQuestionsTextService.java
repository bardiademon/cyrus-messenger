package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapTextType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
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

    /**
     * in javabe tekrari baraye ine ke ye option entekhab shode ama dobare darkhast baraye on option entekhabi miyad
     * <p>
     * ya user ye option entekhab karde bad dobare hamon user hamon option entekhabi ghabli ro entekhab mikone
     * </p>
     * barasi mikonim bebinim vojod dare ya na
     *
     * @see AnswerQuestionsTextRepository#findByMainAccountIdAndQuestionTextIdAndOptionId(long , long , long)
     */
    public AnswerQuestionsText duplicateAnswer (final long userId , final long questionTextId , final long optionId)
    {
        return Repository.findByMainAccountIdAndQuestionTextIdAndOptionId (userId , questionTextId , optionId);
    }

    public long limCount (final long questionTextId)
    {
        return Repository.countByQuestionTextId (questionTextId);
    }

    public AnswersCountYesNo getAnswersYesNo (final long questionTextId)
    {
        return new AnswersCountYesNo (Repository.countByIdAndYesIsTrueAndType (questionTextId , GapTextType.question_yes_no) ,
                Repository.countByIdAndYesIsFalseAndType (questionTextId , GapTextType.question_yes_no));
    }

    public List <AnswersOptions> countQuestionTextOptions (final long questionTextId)
    {
        final List <Object[]> objects = Repository.countQuestionTextOptions (questionTextId);

        if (objects != null && objects.size () > 0)
        {
            /**
             * bar asa selecti ke dakhel Repository shode meghdar dehi shodan
             *
             * @see AnswerQuestionsTextRepository
             * @see AnswerQuestionsTextRepository#countQuestionTextOptions(long)
             */
            final int INDEX_COUNT = 0, INDEX_OPTION_ID = 1;

            List <AnswersOptions> answersOptions = new ArrayList <> ();
            for (final Object[] object : objects)
                answersOptions.add (new AnswersOptions ((long) object[INDEX_OPTION_ID] , (long) object[INDEX_COUNT]));

            objects.clear ();
            System.gc ();

            return answersOptions;
        }

        /*
         * inja biyad yani ke (objects == null || objects.size () == 0)
         */
        return null;
    }

    public static class AnswersCountYesNo
    {
        @JsonProperty ("yes")
        public final long countAnswerYes;

        @JsonProperty ("no")
        public final long countAnswerNo;

        /*
         * baraye ke khatej in class new nashe
         */
        private AnswersCountYesNo ()
        {
            this (0 , 0);
        }

        private AnswersCountYesNo (final long countAnswerYes , final long countAnswerNo)
        {
            this.countAnswerYes = countAnswerYes;
            this.countAnswerNo = countAnswerNo;
        }
    }

    public static class AnswersOptions
    {
        @JsonProperty ("option_id")
        private long optionId;

        @JsonProperty ("count")
        private long count;

        private AnswersOptions ()
        {
        }

        private AnswersOptions (final long OptionId , final long Count)
        {
            this.optionId = OptionId;
            this.count = Count;
        }

        public long getOptionId ()
        {
            return optionId;
        }

        public void setOptionId (long optionId)
        {
            this.optionId = optionId;
        }

        public long getCount ()
        {
            return count;
        }

        public void setCount (long count)
        {
            this.count = count;
        }
    }


}
