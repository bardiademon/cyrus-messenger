package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.GetMessages;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultKey;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapTextType;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText.AnswerQuestionsText;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText.AnswerQuestionsTextService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionText;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.Pagination;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class UserGetMessages
{

    private final GapsService gapsService;
    private final MainAccount user;
    private final PersonalGaps personalGaps;
    private final int page;
    private final AnswerGetMessages answerGetMessages;
    private final RequestGetMessages request;

    private AnswerToClient answer;

    public UserGetMessages
            (final MainAccount User ,
             final PersonalGaps _PersonalGaps ,
             final int Page ,
             final AnswerGetMessages _AnswerGetMessages ,
             final RequestGetMessages _RequestGetMessages)
    {
        this.user = User;
        this.personalGaps = _PersonalGaps;
        this.page = Page;
        this.answerGetMessages = _AnswerGetMessages;
        this.request = _RequestGetMessages;
        this.gapsService = This.GetService (GapsService.class);
        get ();
        answer ();
    }

    private void get ()
    {
        final long size = gapsService.Repository.countGaps (personalGaps.getId () , user.getId ());

        if (size > 0)
        {
            final Integer maxGet = (This.GetService (DefaultService.class)).getInt (DefaultKey.max_get_gaps);
            if (maxGet != null)
            {
                final Pagination.Answer paginationAnswer = This.getServer ().getServerSocketGap ().getPagination ().computing (page , size , maxGet);
                final List <Gaps> privateGaps = gapsService.getPrivateGaps (user.getId () , personalGaps , paginationAnswer);
                if (privateGaps != null && privateGaps.size () > 0)
                {
                    final JSONArray gaps = new JSONArray ();
                    for (final Gaps privateGap : privateGaps)
                        gaps.put (gapToJson (privateGap , null));

                    System.out.println (gaps);

                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());
                    answer.put (KeyAnswer.gaps.name () , gaps.toString ());
                    l.n (ToJson.To (request) , EventName.get_messages.name () , user , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.found.name ()) , null);
                }
                else
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found.name ());
                    l.n (ToJson.To (request) , EventName.get_messages.name () , user , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null);
                }
            }
            else
            {
                answer = AnswerToClient.ServerError ();
                l.n (ToJson.To (request) , EventName.get_messages.name () , user , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , null);
            }
        }
        else
        {
            answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found.name ());
            l.n (ToJson.To (request) , EventName.get_messages.name () , user , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null);
        }
    }

    private JSONObject gapToJson (final Gaps gap , final MainAccount messageForwardedFor)
    {
        try
        {
            if (!gap.isForwarded ())
            {
                final JSONObject gaps = new JSONObject ();

                /*
                 * question text ro migirim
                 */
                final QuestionText questionText = (This.GetService (QuestionTextService.class)).Repository.findByGapsId (gap.getId ());

                /*
                 * age != null bashe yani in gap ye question text hast
                 */
                if (questionText != null)
                {

                    gaps.put (KeyAnswer.is_question_text.name () , true);

                    final JSONObject questionTextInfo = new JSONObject ();

                    questionTextInfo.put (KeyAnswer.question.name () , questionText.getQuestion ());
                    questionTextInfo.put (KeyAnswer.is_yes_no.name () , questionText.isYesNo ());

                    if (!questionText.isYesNo ())
                        questionTextInfo.put (KeyAnswer.question_text_options.name () , ToJson.To (questionText.getOptions ()));

                    questionTextInfo.put (KeyAnswer.is_multiple_choices.name () , questionText.isMultipleChoices ());

                    final AnswerQuestionsTextService answerQuestionsTextService = This.GetService (AnswerQuestionsTextService.class);

                    long lim = answerQuestionsTextService.limCount (questionText.getId ());

                    questionTextInfo.put (KeyAnswer.lim.name () , questionText.getLim ());
                    questionTextInfo.put (KeyAnswer.number_of_answers.name () , lim);
                    questionTextInfo.put (KeyAnswer.time_end.name () , questionText.getUntilThe ());

                    /**
                     * barasi mikonam bebinam baste shode ya na
                     * zamani baste hast ke lim >= lim taeen shode bashe , ya zamane pasokh tamom shode bashe
                     *
                     * @see QuestionText
                     * @see AnswerQuestionsText
                     */
                    questionTextInfo.put (KeyAnswer.closed.name () , (lim >= questionText.getLim () || Time.BiggerNow (questionText.getUntilThe ())));

                    if (questionText.isYesNo ())
                        questionTextInfo.put (KeyAnswer.answers.name () , answerQuestionsTextService.getAnswersYesNo (questionText.getId ()));
                    else
                    {
                        if (gap.getTextType () != null && gap.getTextType ().equals (GapTextType.question_options))
                            questionTextInfo.put (KeyAnswer.answers.name () , ToJson.To (answerQuestionsTextService.countQuestionTextOptions (questionText.getId ())));
                    }
                    gaps.put (KeyAnswer.question_text.name () , questionTextInfo);
                }

                gaps.put (KeyAnswer.id.name () , gap.getId ());
                gaps.put (KeyAnswer.text.name () , gap.getText ());
                gaps.put (KeyAnswer.from.name () , gap.getFrom ().getId ());
                gaps.put (KeyAnswer.to.name () , gap.getToUser ().getId ());
                gaps.put (KeyAnswer.gap_types.name () , new JSONArray (ToJson.To (gap.getGapTypes ())));

                if (messageForwardedFor != null)
                    gaps.put (KeyAnswer.forwarded.name () , messageForwardedFor.getId ());

                final List <GapsFiles> filesGaps = gap.getFilesGaps ();
                if (filesGaps != null && filesGaps.size () > 0)
                {
                    final JSONArray filesGapsCode = new JSONArray ();
                    for (GapsFiles filesGap : filesGaps) filesGapsCode.put (filesGap.getCode ());
                    gaps.put (KeyAnswer.files.name () , filesGapsCode);
                }

                if (messageForwardedFor == null && gap.getReply () != null)
                {
                    final Gaps reply = gap.getReply ();

                    final JSONObject gapReply = new JSONObject ();
                    gapReply.put (KeyAnswer.text.name () , reply.getText ());
                    gapReply.put (KeyAnswer.reply_id.name () , reply.getId ());
                    gapReply.put (KeyAnswer.has_file.name () , (reply.getFilesGaps () != null));
                    gaps.put (KeyAnswer.reply.name () , gapReply);
                }
                return gaps;
            }
            else return gapToJson (gap.getForwarded ().getGap () , gap.getForwarded ().getMessageFrom ());
        }
        catch (JSONException e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e);
            return null;
        }
    }

    private enum KeyAnswer
    {
        id, text, from, to, forwarded, has_file, files, reply, gap_types,

        gaps, reply_id,

        /*
         * for question text
         */
        is_question_text, question_text,
        is_yes_no, question_text_options, question, is_multiple_choices, closed, time_end, lim, number_of_answers, answers
    }

    private void answer ()
    {
        answerGetMessages.Answer (answer);
    }
}
