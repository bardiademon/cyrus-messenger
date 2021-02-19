package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapTextType;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionText;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextOptions.QuestionTextOptions;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

/*
 * in table baraye ine ke javabe question text ha ro inja zakhire konam
 * mojaza va daastresi rahat
 *
 * javab ham yani ke har karbar che javabi be har soal dade
 */
@Entity
@Table (name = "answer_questions_text")
public final class AnswerQuestionsText
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private MainAccount mainAccount;

    @ManyToOne
    @JoinColumn (name = "question_text_id", referencedColumnName = "id")
    private QuestionText questionText;

    /*
     * baraye soal haye yes no , null hast baraye digar soal ha
     * agar true bashe yani yes entekhab shode
     */
    @Column (name = "yes_no")
    private boolean yes;

    /*
     * baraye soal haei ke chand option daran
     *
     * mitone null bashe baraye soal haei ke niyaz be option nadare
     *
     * yeki az option ha sakhire mishe , agar soal chand entekhabi bashe option haye badi dar row jadid sakhire mishan
     */
    @ManyToOne
    @JoinColumn (name = "option_id", referencedColumnName = "id")
    private QuestionTextOptions option;

    @Column (name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    /*
     * in ro baraye in mizaram ke betonam tashkhis bedam type haro az ham
     *
     * masal benotam question haye yes no ro tedad ro begiram
     */
    @Column (name = "question_text_type")
    @Enumerated (EnumType.STRING)
    private GapTextType type = GapTextType.normal;

    public AnswerQuestionsText ()
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

    public MainAccount getMainAccount ()
    {
        return mainAccount;
    }

    public void setMainAccount (MainAccount mainAccount)
    {
        this.mainAccount = mainAccount;
    }

    public QuestionText getQuestionText ()
    {
        return questionText;
    }

    public void setQuestionText (QuestionText questionText)
    {
        this.questionText = questionText;
    }

    public boolean isYes ()
    {
        return yes;
    }

    public void setYes (boolean yes)
    {
        this.yes = yes;
    }

    public QuestionTextOptions getOption ()
    {
        return option;
    }

    public void setOption (QuestionTextOptions option)
    {
        this.option = option;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public GapTextType getType ()
    {
        return type;
    }

    public void setType (GapTextType type)
    {
        this.type = type;
    }
}
