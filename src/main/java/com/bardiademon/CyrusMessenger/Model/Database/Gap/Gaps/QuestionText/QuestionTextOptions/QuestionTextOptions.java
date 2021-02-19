package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextOptions;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionText;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
 * option ha be tartip bayad namayesh dade shan
 *  pas baraye option ha in table mojaza hamra ba index ro sakhtam
 */
@Entity
@Table (name = "question_text_options")
public final class QuestionTextOptions
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    @JsonIgnore
    private long id;

    @Column (name = "option_text")
    private String optionText;

    @Column (name = "index_option", nullable = false)
    private int index;

    @ManyToOne
    @JoinColumn (name = "question_text_id", referencedColumnName = "id")
    @JsonIgnore
    private QuestionText questionText;

    public QuestionTextOptions ()
    {
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public String getOptionText ()
    {
        return optionText;
    }

    public void setOptionText (String optionText)
    {
        this.optionText = optionText;
    }

    public int getIndex ()
    {
        return index;
    }

    public void setIndex (int index)
    {
        this.index = index;
    }

    public QuestionText getQuestionText ()
    {
        return questionText;
    }

    public void setQuestionText (QuestionText questionText)
    {
        this.questionText = questionText;
    }
}
