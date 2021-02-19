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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        this.gapsService = This.Services ().Get (GapsService.class);
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
                    final List <Map <String, Object>> gaps = new ArrayList <> ();
                    for (Gaps privateGap : privateGaps)
                        gaps.add (gapToMap (privateGap , null));

                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());
                    answer.put (KeyAnswer.gaps.name () , gaps);
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

    private Map <String, Object> gapToMap (Gaps gap , MainAccount messageForwardedFor)
    {
        if (!gap.isForwarded ())
        {
            final ToJson.CreateClass createClass = new ToJson.CreateClass ();

            /*
             * question text ro migirim
             */
            final QuestionText questionText = (This.GetService (QuestionTextService.class)).Repository.findByGapsId (gap.getId ());

            /*
             * age != null bashe yani in gap ye question text hast
             */
            if (questionText != null)
            {
                createClass.put (KeyAnswer.is_question_text.name () , true);

                ToJson.CreateClass questionTextInfo = new ToJson.CreateClass ();

                questionTextInfo.put (KeyAnswer.question.name () , questionText.getQuestion ());
                questionTextInfo.put (KeyAnswer.is_yes_no.name () , questionText.isYesNo ());

                if (!questionText.isYesNo ())
                    questionTextInfo.put (KeyAnswer.question_text_options.name () , questionText.getOptions ());

                questionTextInfo.put (KeyAnswer.is_multiple_choices.name () , questionText.isMultipleChoices ());

                AnswerQuestionsTextService answerQuestionsTextService = This.GetService (AnswerQuestionsTextService.class);

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
                    createClass.put (KeyAnswer.answers.name () , answerQuestionsTextService.getAnswersYesNo (questionText.getId ()));
                else
                {
                    if (gap.getTextType ().equals (GapTextType.question_options))
                        createClass.put (KeyAnswer.answers.name () , ToJson.To (answerQuestionsTextService.countQuestionTextOptions (questionText.getId ())));
                }

                createClass.put (KeyAnswer.question_text.name () , questionTextInfo);
            }

            createClass.put (KeyAnswer.id.name () , gap.getId ());
            createClass.put (KeyAnswer.text.name () , gap.getText ());
            createClass.put (KeyAnswer.from.name () , gap.getFrom ().getId ());
            createClass.put (KeyAnswer.to.name () , gap.getToUser ().getId ());
            createClass.put (KeyAnswer.gap_types.name () , gap.getGapTypes ());

            if (messageForwardedFor != null)
                createClass.put (KeyAnswer.forwarded.name () , messageForwardedFor.getId ());

            final List <GapsFiles> filesGaps = gap.getFilesGaps ();
            if (filesGaps != null && filesGaps.size () > 0)
            {
                List <String> filesGapsCode = new ArrayList <> ();
                for (GapsFiles filesGap : filesGaps) filesGapsCode.add (filesGap.getCode ());
                createClass.put (KeyAnswer.files.name () , filesGapsCode);
            }

            if (messageForwardedFor == null && gap.getReply () != null)
            {
                Gaps reply = gap.getReply ();

                ToJson.CreateClass gapReply = new ToJson.CreateClass ();
                gapReply.put (KeyAnswer.text.name () , reply.getText ());
                gapReply.put (KeyAnswer.reply_id.name () , reply.getId ());
                gapReply.put (KeyAnswer.has_file.name () , (reply.getFilesGaps () != null));

                createClass.put (KeyAnswer.reply.name () , gapReply);
            }
            return createClass.getCreateClass ();
        }
        else return gapToMap (gap.getForwarded ().getGap () , gap.getForwarded ().getMessageFrom ());
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
