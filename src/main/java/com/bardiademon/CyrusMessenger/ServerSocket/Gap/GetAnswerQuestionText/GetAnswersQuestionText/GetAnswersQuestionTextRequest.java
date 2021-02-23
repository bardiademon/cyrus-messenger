package com.bardiademon.CyrusMessenger.ServerSocket.Gap.GetAnswerQuestionText.GetAnswersQuestionText;

import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.PublicRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class GetAnswersQuestionTextRequest extends PublicRequest
{
    @JsonProperty ("question_text_id")
    private long questionTextId;

    @JsonProperty ("gap_id")
    private long gapId;

    @JsonProperty ("personalGapId")
    private long personalGapId;

    public GetAnswersQuestionTextRequest ()
    {
        super ();
    }

    public long getQuestionTextId ()
    {
        return questionTextId;
    }

    public void setQuestionTextId (long questionTextId)
    {
        this.questionTextId = questionTextId;
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
}
