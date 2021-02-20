package com.bardiademon.CyrusMessenger.ServerSocket.Gap.GetAnswerQuestionText;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient.CUV;
import static com.bardiademon.CyrusMessenger.Controller.AnswerToClient.CUV.recorded;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapTextType;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText.AnswerQuestionsText;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText.AnswerQuestionsTextService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionText;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextOptions.QuestionTextOptions;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextOptions.QuestionTextOptionsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextService;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.CheckPublicRequest.Client;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public final class GetAnswerQuestionText
{

    public void answerQuestionText (final GetAnswerQuestionTextRequest request , final Client client)
    {
        AnswerToClient answer = null;
        if (client.ok)
        {
            /**
             * barasi mikonam ke gap id dorost hast ya na
             * @see ID
             */
            final ID gapId = new ID (request.getGapId ());
            if (gapId.isValid ())
            {
                /**
                 * barasi mikonam ke personal gap id dorost hast ya na
                 * @see ID
                 */
                final ID personalGapId = new ID (request.getPersonalGapId ());
                if (personalGapId.isValid ())
                {
                    /**
                     *
                     * barasi mikonam ke question text id dorost hast ya na
                     * @see ID
                     */
                    final ID questionTextId = new ID (request.getQuestionTextId ());

                    if (questionTextId.isValid ())
                    {
                        /**
                         * GapsService ro migiram
                         * @see This
                         */
                        final GapsService gapsService = This.GetService (GapsService.class);

                        /*
                         * barasi mokonam bebinam gap id va personal gap id va jarbari ke darkhast ro dade ba ham motabeghat daran ya na
                         * bayad se id ersali ba ham eko bashand
                         */
                        final Gaps gaps = gapsService.byId (gapId.getId () , personalGapId.getId () , client.getMainAccount ().getId ());
                        if (gaps != null)
                        {
                            /*
                             * inja ham question text ro ba gap id migiram
                             */
                            final QuestionTextService questionTextService = This.GetService (QuestionTextService.class);
                            final QuestionText questionText = questionTextService.byId (questionTextId.getId () , gaps.getId ());
                            if (questionText != null)
                            {
                                final JSONArray optionsIdArray = new JSONArray (request.getOptionId ());

                                /*
                                 * agar type soal yes va no bashe , request options id ha != null bod darkhast moshkel dare
                                 *
                                 * yani ke vaghti type yes o no hast bayad option id az request == null ya size == 0 bashe
                                 */
                                if (questionText.isYesNo () && (request.getOptionId () != null && request.getOptionId ().length () > 0))
                                {
                                    answer = AnswerToClient.BadRequest ();
                                    l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception ("see description") , "if (questionText.isYesNo () && (questionText.getOptions () != null && questionText.getOptions ().size () > 0))");
                                }
                                else
                                {
                                    final AnswerQuestionsTextService answerQuestionsTextService = This.GetService (AnswerQuestionsTextService.class);

                                    /*
                                     * mahdode pasokh be question hast
                                     *
                                     * yani alan question sapt mishe bayad ta farda hamin saet pasokh dade beshe agar bishtar shod baste mishe question
                                     */
                                    if (!Time.BiggerNow (questionText.getUntilThe ()))
                                    {
                                        /*
                                         * bayad tedad afradi ke pasokh dadan az lim ke taeen shode kamtar bashe
                                         *
                                         * betor mesal agar lim 100 bashe va kole javab ha 99 bashe in karbar mitone pasokh bede
                                         *
                                         * ama agar lim 100 bashe pasokhha ham 100 dige baste shode in question text
                                         *
                                         * agar ham == 0 bashe yani lim tarif nashode pas har tedad karbar ke bashe mitonan pasokh bedan
                                         */
                                        if (questionText.getLim () == 0 || answerQuestionsTextService.limCount (questionText.getId ()) < questionText.getLim ())
                                        {
                                            final List <AnswerQuestionsText> answerQuestionsTexts = answerQuestionsTextService.userAnswers (client.getMainAccount ().getId () , questionText.getId ());

                                            if (questionText.isYesNo ())
                                            {
                                                /*
                                                 * bayad answer sabt nakarde bashe pas == 0 yani sabt nakarde
                                                 */
                                                if (answerQuestionsTexts.size () == 0)
                                                {
                                                    final AnswerQuestionsText answerQuestionsText = new AnswerQuestionsText ();
                                                    answerQuestionsText.setYes (true);
                                                    answerQuestionsText.setMainAccount (client.getMainAccount ());
                                                    answerQuestionsText.setQuestionText (questionText);
                                                    answerQuestionsText.setType (GapTextType.question_yes_no);
                                                    answerQuestionsTextService.Repository.save (answerQuestionsText);
                                                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , recorded.name ());
                                                    l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (recorded.name ()) , null);
                                                }
                                                else
                                                {
                                                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.found_answer.name ());
                                                    l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.found_answer.name ()) , null);
                                                }
                                            }
                                            else
                                            {
                                                /*
                                                 * agar chand entekhabi faal nabashe va entekhab karde bashe khata darim
                                                 */
                                                if (!questionText.isMultipleChoices () && answerQuestionsTexts.size () > 0)
                                                {
                                                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.found_answer.name ());
                                                    l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.found_answer.name ()) , null);
                                                }
                                                else
                                                {


//                                                    final List <Long> optionsId = request.getOptionId ();
                                                    final QuestionTextOptionsService questionTextOptionsService = This.GetService (QuestionTextOptionsService.class);

                                                    final List <QuestionTextOptions> options = new ArrayList <> ();

                                                    /*
                                                     * dakhel for age khataee pish omad edame code ejra nabad beshe
                                                     * edame code bad az for
                                                     */
                                                    boolean ok = true;

                                                    final boolean checkDuplicateAnswer = answerQuestionsTexts.size () > 0;
                                                    for (int i = 0, len = optionsIdArray.length (); i < len; i++)
                                                    {
                                                        final long optionId = optionsIdArray.getLong (i);
                                                        final QuestionTextOptions option;

                                                        if ((option = questionTextOptionsService.byId (questionText.getId () , optionId)) == null)
                                                        {
                                                            /*
                                                             * inja ke miyad yani yeki az option id ha peyda nashode
                                                             */
                                                            answer = AnswerToClient.IdInvalid (ValAnswer.invalid_option_id.name ());
                                                            answer.put (AnswerToClient.CUK.which.name () , optionId);
                                                            l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (CUV.id_invalid.name ()) , ToJson.CreateClass.nj ("option_id" , optionId));

                                                            ok = false;
                                                            break;
                                                        }
                                                        else
                                                        {
                                                            /**
                                                             * @see AnswerQuestionsTextService#duplicateAnswer(long , long , long)
                                                             */
                                                            if (checkDuplicateAnswer && answerQuestionsTextService.duplicateAnswer (client.getMainAccount ().getId () , questionText.getId () , optionId) != null)
                                                            {
                                                                answer = AnswerToClient.IdInvalid (ValAnswer.duplicate_answer.name ());
                                                                answer.put (AnswerToClient.CUK.which.name () , optionId);
                                                                l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (CUV.id_invalid.name ()) , ToJson.CreateClass.nj ("option_id" , optionId));

                                                                ok = false;
                                                                break;
                                                            }
                                                            else options.add (option);
                                                        }
                                                    }

                                                    if (ok)
                                                    {
                                                        /*
                                                         * inja hamechiz doroste va amade zakhire kardan answer hast
                                                         */

                                                        for (final QuestionTextOptions option : options)
                                                        {
                                                            final AnswerQuestionsText answerQuestionsText = new AnswerQuestionsText ();
                                                            answerQuestionsText.setQuestionText (questionText);
                                                            answerQuestionsText.setMainAccount (client.getMainAccount ());
                                                            answerQuestionsText.setOption (option);
                                                            answerQuestionsText.setType (GapTextType.question_options);
                                                            answerQuestionsTextService.Repository.save (answerQuestionsText);
                                                        }
                                                        answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , recorded.name ());
                                                        l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (recorded.name ()) , null);
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.completed.name ());
                                            l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.completed.name ()) , String.valueOf (questionText.getLim ()));
                                        }
                                    }
                                    else
                                    {
                                        /*
                                         * in ja ke miyad yani zamane pasokh dadan be in question tamam shode ast
                                         */
                                        answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.the_response_time_has_passed.name ());
                                        l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.the_response_time_has_passed.name ()) , questionText.getUntilThe ().toString ());
                                    }
                                }
                            }
                            else
                            {
                                answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found_question_text.name ());
                                l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_found_question_text.name ()) , null);
                            }
                        }
                        else
                        {
                            answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found_gap_or_personal_gap.name ());
                            l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_found_gap_or_personal_gap.name ()) , null);
                        }
                    }
                    else
                    {
                        answer = AnswerToClient.IdInvalid ();
                        answer.put (AnswerToClient.CUK.which.name () , ValAnswer.question_text_id.name ());
                        l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (CUV.id_invalid.name ()) , ValAnswer.question_text_id.name ());
                    }
                }
                else
                {
                    answer = AnswerToClient.IdInvalid ();
                    answer.put (AnswerToClient.CUK.which.name () , ValAnswer.personal_gap_id.name ());
                    l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (CUV.id_invalid.name ()) , ValAnswer.personal_gap_id.name ());
                }
            }
            else
            {
                answer = AnswerToClient.IdInvalid ();
                answer.put (AnswerToClient.CUK.which.name () , ValAnswer.gap_id.name ());
                l.n (ToJson.To (request) , EventName.ssg_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (CUV.id_invalid.name ()) , ValAnswer.gap_id.name ());
            }

            sendToClient (answer , client);
        }
    }

    private void sendToClient (final AnswerToClient answer , final Client client)
    {
        client.online.getClient ().sendEvent (EventName.e_ssg_answer_question_text.name () , ToJson.To (answer));
        l.n (Thread.currentThread ().getStackTrace () , ToJson.CreateClass.n ("send_to_client" , true).put ("user_id" , client.getMainAccount ()).put ("answer_to_client" , ToJson.To (answer)).toJson ());
    }

    private enum ValAnswer
    {
        gap_id, invalid_option_id, personal_gap_id, question_text_id, not_found_gap_or_personal_gap, not_found_question_text, found_answer, the_response_time_has_passed,

        /**
         * @see AnswerQuestionsTextService#duplicateAnswer(long , long , long)
         */
        duplicate_answer,

        /*
         * in ro vaghti mifrestam ke lim (tedad afradi ke motonan pasokh bedan be question text) por shode bashe
         */
        completed
    }
}
