package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextOptions.QuestionTextOptions;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name = "question_text")
public final class QuestionText
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @Column (name = "question_text", nullable = false)
    private String question;

    @Column (name = "yes_no", nullable = false)
    private boolean yesNo;

    /*
     * dar sorati ke soal yes no nabashe , listi az gozine ha hast
     */
    @OneToMany (mappedBy = "questionText")
    @Column (name = "options_question")
    private List <QuestionTextOptions> options;

    /*
     * yani beshe chanta gozine entekhab kard
     * dar sorati ke soal yes no nabashe
     */
    @Column (name = "multiple_choices")
    private boolean multipleChoices;

    /*
     * lim => Mahdodiyat (Pinglish)
     * baraye ke afrade sherkat konande ro mahdod konam , maslan 20 nafar faghat mitinan sherkat konan
     */
    @Column (name = "lim_question", nullable = false)
    private long lim = 0;

    @Column (name = "until_the_question")
    private LocalDateTime untilThe;

    @OneToOne
    @JoinColumn (name = "gap_id", referencedColumnName = "id")
    private Gaps gaps;

    public QuestionText ()
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

    public String getQuestion ()
    {
        return question;
    }

    public void setQuestion (String question)
    {
        this.question = question;
    }

    public boolean isYesNo ()
    {
        return yesNo;
    }

    public void setYesNo (boolean yesNo)
    {
        this.yesNo = yesNo;
    }

    public List <QuestionTextOptions> getOptions ()
    {
        return options;
    }

    public void setOptions (List <QuestionTextOptions> oprions)
    {
        this.options = oprions;
    }

    public boolean isMultipleChoices ()
    {
        return multipleChoices;
    }

    public void setMultipleChoices (boolean multipleChoices)
    {
        this.multipleChoices = multipleChoices;
    }

    public long getLim ()
    {
        return lim;
    }

    public void setLim (long lim)
    {
        this.lim = lim;
    }

    public LocalDateTime getUntilThe ()
    {
        return untilThe;
    }

    public void setUntilThe (LocalDateTime untilThe)
    {
        this.untilThe = untilThe;
    }

    public Gaps getGaps ()
    {
        return gaps;
    }

    public void setGaps (Gaps gaps)
    {
        this.gaps = gaps;
    }
}
