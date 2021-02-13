package com.bardiademon.CyrusMessenger.ServerSocket.Gap.GetAnswerQuestionText;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText.AnswerQuestionsText;
import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.PublicRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public final class GetAnswerQuestionTextRequest extends PublicRequest
{
    @JsonProperty ("gap_id")
    private long gapId;

    @JsonProperty ("personal_gap_id")
    private long personalGapId;

    @JsonProperty ("question_text_id")
    private long questionTextId;

    /**
     * dar table AnswerQuestionText tozih dade shode
     *
     * @see AnswerQuestionsText
     */
    private boolean yes;

    /**
     * dar table AnswerQuestionText tozih dade shode
     *
     * @see AnswerQuestionsText
     */
    @JsonProperty ("option_id")
    private List <Long> optionId;

    public GetAnswerQuestionTextRequest ()
    {
        super ();
    }

    public long getGapId ()
    {
        return gapId;
    }

    public void setGapId (long gapId)
    {
        this.gapId = gapId;
    }

    public long getPersonalGapId ()
    {
        return personalGapId;
    }

    public void setPersonalGapId (long personalGapId)
    {
        this.personalGapId = personalGapId;
    }

    public long getQuestionTextId ()
    {
        return questionTextId;
    }

    public void setQuestionTextId (long questionTextId)
    {
        this.questionTextId = questionTextId;
    }

    public boolean isYes ()
    {
        return yes;
    }

    public void setYes (boolean yes)
    {
        this.yes = yes;
    }

    public List <Long> getOptionId ()
    {
        return optionId;
    }

    public void setOptionId (List <Long> optionId)
    {
        this.optionId = optionId;
    }
}
