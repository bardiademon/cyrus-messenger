package com.bardiademon.CyrusMessenger.ServerSocket.Gap;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserGapAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapTextType;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionText;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextOptions.QuestionTextOptions;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public final class CheckGapText
{
    private final String text;
    private final String strTextType;
    private final UserGapAccessLevel gapAccessLevel;

    private GapTextType textType;

    private AnswerToClient answer;

    private JSONObject jsonQuestionText;

    private String textOrQuestion;
    private LocalDateTime untilThe;
    private long lim;
    private List <QuestionTextOptions> options;
    private boolean multipleChoices;

    private QuestionText questionText;

    public CheckGapText (final String Text , final String TextType , final UserGapAccessLevel _UserGapAccessLevel)
    {
        this.text = Text;
        this.strTextType = TextType;
        this.gapAccessLevel = _UserGapAccessLevel;
    }

    public boolean idValid ()
    {
        checkTextType ();
        if (answer == null)
        {
            if (textType.equals (GapTextType.normal)) return true;

            /*
             * barasi ke karbari ke barash mikhad questionText ersal beshe dastrese be darkhast konande dade ya na
             */
            if (gapAccessLevel.hasAccess (Which.s_question_text))
            {
                checkQuestionTextYesNo ();
                if (textType.equals (GapTextType.question_options)) checkQuestionText ();

                /*
                 * age answer != null bashe yani khataei rokh dade pas dige nabayad bagiye code ejra beshe
                 */
                if (answer == null) toQuestionText ();
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.AccessDenied () , ValAnswer.access_denied_question_text.name ());
                l.n (null , null , gapAccessLevel.getApplicant () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.access_denied_question_text.name ()) , null);
            }
        }

        return ok ();
    }

    private void checkTextType ()
    {
        if ((textType = GapTextType.to (strTextType)) == null)
        {
            answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_text_type.name ());
            l.n (null , null , null , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_text_type.name ()) , null);
        }
    }

    private void checkQuestionTextYesNo ()
    {
        try
        {
            /*
             * faghat mizaram getString , pas agar question nabashe khodesh kare trows exception ro mikone
             */
            textOrQuestion = (jsonQuestionText = new JSONObject (text)).getString ("question");

            /*
             * until the => ta (Pinglish)
             * yani ta key mohlate pasokhdehi hast
             */
            untilThe = new Timestamp (jsonQuestionText.getLong ("until_the")).toLocalDateTime ();

            /*
             * lim => Mahdodiyat (Finglish)
             * baraye ke afrade sherkat konande ro mahdod konam , maslan 20 nafar faghat mitinan sherkat konan
             */
            lim = jsonQuestionText.getLong ("lim");
        }
        catch (Exception e)
        {
            answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_text_question_yes_no.name ());
            l.n (null , null , null , answer , Thread.currentThread ().getStackTrace () , e , ValAnswer.invalid_text_question_yes_no.name ());
        }
    }

    private void checkQuestionText ()
    {
        try
        {
            /*
             * faghat mizaram getString , pas agar question nabashe khodesh kare trows exception ro mikone
             */
            final JSONArray optionsJsonArray = jsonQuestionText.getJSONArray ("options");

            /*
             * listi az options ha hast , , soal gozinei ye seri gozine baraye entekhab dare
             * in qozine ha dar khaleb ye JSONArray ersal mishan be server va dar ghaleb ye table Options sakhire mishan
             */
            if (optionsJsonArray.length () > 0)
            {
                options = new ArrayList <> ();
                for (int i = 0, len = optionsJsonArray.length (); i < len; i++)
                {
                    final JSONObject jsoOption = optionsJsonArray.getJSONObject (i);
                    final QuestionTextOptions option = new QuestionTextOptions ();

                    /*
                     * manzor az index indeke , har option be tertib nemayesh dade beshe ,
                     * baraye ke vaght nemayesh in tartip hefz beshe
                     */
                    option.setIndex (jsoOption.getInt ("index"));
                    option.setOptionText (jsoOption.getString ("option_text"));
                    options.add (option);
                }
            }
            else throw new Exception ("Option size is 0");

            /*
             * yani beshe chanta gozine entekhab kard
             */
            multipleChoices = jsonQuestionText.getBoolean ("multiple_choices");
        }
        catch (Exception e)
        {
            answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_text_question_yes_no.name ());
            l.n (null , null , null , answer , Thread.currentThread ().getStackTrace () , e , ValAnswer.invalid_text_question_yes_no.name ());
        }
    }

    private void toQuestionText ()
    {
        questionText = new QuestionText ();
        questionText.setLim (lim);
        questionText.setMultipleChoices (multipleChoices);
        questionText.setQuestion (textOrQuestion);
        questionText.setUntilThe (untilThe);
        questionText.setYesNo (textType.equals (GapTextType.question_yes_no));
    }

    public boolean ok ()
    {
        return (answer == null);
    }

    public AnswerToClient getAnswer ()
    {
        return answer;
    }

    public QuestionText getQuestionText ()
    {
        return questionText;
    }

    public List <QuestionTextOptions> getOptions ()
    {
        return options;
    }

    public boolean isQuestionText ()
    {
        return (getQuestionText () != null);
    }

    private enum ValAnswer
    {
        invalid_text_type, invalid_text_question_yes_no, link, access_denied_question_text
    }

    public String getTextOrQuestion ()
    {
        return textOrQuestion;
    }

    private enum KeyAnswer
    {
        code, which
    }
}
