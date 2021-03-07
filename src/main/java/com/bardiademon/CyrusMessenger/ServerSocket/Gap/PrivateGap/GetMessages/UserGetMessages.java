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
import com.bardiademon.CyrusMessenger.bardiademon.CyrusJSON;
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
            final DefaultService.Value <Integer> maxGet = (This.GetService (DefaultService.class)).integerValue (DefaultKey.max_get_gaps);
            if (maxGet.ok)
            {
                final Pagination.Answer paginationAnswer = This.getServer ().getServerSocketGap ().getPagination ().computing (page , size , maxGet.value);
                final List <Gaps> privateGaps = gapsService.getPrivateGaps (user.getId () , personalGaps , paginationAnswer);
                if (privateGaps != null && privateGaps.size () > 0)
                {
                    final JSONArray gaps = new JSONArray ();
                    for (final Gaps privateGap : privateGaps)
                        gaps.put (gapToJson (privateGap , null));

                    System.out.println (gaps);

                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found);
                    answer.put (KeyAnswer.gaps , gaps.toString ());
                    l.n (ToJson.To (request) , EventName.get_messages.name () , user , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.found));
                }
                else
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found);
                    l.n (ToJson.To (request) , EventName.get_messages.name () , user , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found));
                }
            }
            else
            {
                answer = maxGet.answer;
                l.n (ToJson.To (request) , EventName.get_messages.name () , user , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.please_try_again));
            }
        }
        else
        {
            answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found);
            l.n (ToJson.To (request) , EventName.get_messages.name () , user , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found));
        }
    }

    private JSONObject gapToJson (final Gaps gap , final MainAccount messageForwardedFor)
    {
        try
        {
            if (!gap.isForwarded ())
            {
                final CyrusJSON gaps = new CyrusJSON ();

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

                    final CyrusJSON questionTextInfo = new CyrusJSON ();

                    questionTextInfo.put (KeyAnswer.question , questionText.getQuestion ());
                    questionTextInfo.put (KeyAnswer.is_yes_no , questionText.isYesNo ());

                    if (!questionText.isYesNo ())
                        questionTextInfo.put (KeyAnswer.question_text_options , ToJson.To (questionText.getOptions ()));

                    questionTextInfo.put (KeyAnswer.is_multiple_choices , questionText.isMultipleChoices ());

                    final AnswerQuestionsTextService answerQuestionsTextService = This.GetService (AnswerQuestionsTextService.class);

                    long lim = answerQuestionsTextService.limCount (questionText.getId ());

                    questionTextInfo.put (KeyAnswer.lim , questionText.getLim ());
                    questionTextInfo.put (KeyAnswer.number_of_answers , lim);
                    questionTextInfo.put (KeyAnswer.time_end , questionText.getUntilThe ());

                    /**
                     * barasi mikonam bebinam baste shode ya na
                     * zamani baste hast ke lim >= lim taeen shode bashe , ya zamane pasokh tamom shode bashe
                     *
                     * @see QuestionText
                     * @see AnswerQuestionsText
                     */
                    questionTextInfo.put (KeyAnswer.closed , (lim >= questionText.getLim () || Time.BiggerNow (questionText.getUntilThe ())));

                    if (questionText.isYesNo ())
                        questionTextInfo.put (KeyAnswer.answers , answerQuestionsTextService.getAnswersYesNo (questionText.getId ()));
                    else
                    {
                        if (gap.getTextType () != null && gap.getTextType ().equals (GapTextType.question_options))
                            questionTextInfo.put (KeyAnswer.answers , ToJson.To (answerQuestionsTextService.countQuestionTextOptions (questionText.getId ())));
                    }
                    gaps.put (KeyAnswer.question_text , questionTextInfo);
                }

                gaps.put (KeyAnswer.id , gap.getId ());
                gaps.put (KeyAnswer.text , gap.getText ());
                gaps.put (KeyAnswer.from , gap.getFrom ().getId ());
                gaps.put (KeyAnswer.to , gap.getToUser ().getId ());
                gaps.put (KeyAnswer.gap_types , new JSONArray (ToJson.To (gap.getGapTypes ())));

                if (messageForwardedFor != null)
                    gaps.put (KeyAnswer.forwarded , messageForwardedFor.getId ());

                final List <GapsFiles> filesGaps = gap.getFilesGaps ();
                if (filesGaps != null && filesGaps.size () > 0)
                {
                    final JSONArray filesGapsCode = new JSONArray ();
                    for (GapsFiles filesGap : filesGaps) filesGapsCode.put (filesGap.getCode ());
                    gaps.put (KeyAnswer.files , filesGapsCode);
                }

                if (messageForwardedFor == null && gap.getReply () != null)
                {
                    final Gaps reply = gap.getReply ();

                    final CyrusJSON gapReply = new CyrusJSON ();
                    gapReply.put (KeyAnswer.text , reply.getText ());
                    gapReply.put (KeyAnswer.reply_id , reply.getId ());
                    gapReply.put (KeyAnswer.has_file , (reply.getFilesGaps () != null));
                    gaps.put (KeyAnswer.reply , gapReply);
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
