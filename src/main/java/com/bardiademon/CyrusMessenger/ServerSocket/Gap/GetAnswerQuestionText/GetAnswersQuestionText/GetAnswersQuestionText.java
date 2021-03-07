package com.bardiademon.CyrusMessenger.ServerSocket.Gap.GetAnswerQuestionText.GetAnswersQuestionText;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText.AnswerQuestionsTextService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText.AnswerQuestionsTextService.AnswersCountYesNo;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText.AnswerQuestionsTextService.AnswersOption;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText.AnswerQuestionsTextService.AnswersYesNo;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionText;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextOptions.QuestionTextOptions;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.QuestionTextService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.CheckPublicRequest;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.CheckPublicRequest.Client;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * baraye gereftan javabe question text ha
 */
public final class GetAnswersQuestionText
{
    public void get (final GetAnswersQuestionTextRequest request , final Client client)
    {
        /**
         * @see CheckPublicRequest
         */
        if (client.ok)
        {
            AnswerToClient answer;

            final ID questionTextId = new ID (request.getQuestionTextId ());
            if (questionTextId.isValid ())
            {
                final ID gapId = new ID (request.getGapId ());
                if (gapId.isValid ())
                {
                    final ID personalGapId = new ID (request.getPersonalGapId ());
                    if (personalGapId.isValid ())
                    {
                        final Gaps gaps = This.GetService (GapsService.class).byId (gapId.getId () , personalGapId.getId () , client.getMainAccount ().getId ());
                        if (gaps != null)
                        {
                            final QuestionText questionText = This.GetService (QuestionTextService.class).byId (questionTextId.getId () , gapId.getId ());
                            if (questionText != null)
                            {
                                final UserProfileAccessLevel accessLevel = new UserProfileAccessLevel (client.getMainAccount ());
                                final AnswerQuestionsTextService answerQuestionsTextService = This.GetService (AnswerQuestionsTextService.class);
                                if (questionText.isYesNo ())
                                {
                                    /*
                                     * barasi mikonam bebinam kasi javab dade be in porsesh ua na
                                     */
                                    final AnswersCountYesNo answersYesNo = answerQuestionsTextService.getAnswersYesNo (questionTextId.getId ());
                                    if (answersYesNo.countAnswerNo > 0 || answersYesNo.countAnswerYes > 0)
                                    {
                                        final List <AnswersYesNo> answersYesNos = answerQuestionsTextService.answersYesNo (questionTextId.getId ());
                                        if (answersYesNos.size () > 0)
                                        {
                                            /*
                                             * bayad barasi konim sath dastresi dare in karbar be in karbari ke javab dade ya na
                                             */

                                            answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());

                                            final JSONArray usersIdsYes = new JSONArray ();
                                            final JSONArray usersIdsNo = new JSONArray ();

                                            for (final AnswersYesNo answerYesNo : answersYesNos)
                                            {
                                                accessLevel.setUser (answerYesNo.mainAccount);

                                                /*
                                                 * bayad karbari ke darkhast mide be find me va id in karbar dastrasi dashte bashe
                                                 */
                                                if (accessLevel.hasAccess (Which.find_me , Which.id))
                                                {
                                                    if (answerYesNo.yes)
                                                        usersIdsYes.put (answerYesNo.mainAccount.getId ());
                                                    else
                                                        usersIdsNo.put (answerYesNo.mainAccount.getId ());
                                                }
                                            }

                                            answersYesNos.clear ();
                                            System.gc ();

                                            final JSONObject usersIds = new JSONObject ();
                                            usersIds.put (ValAnswer.users_yes.name () , usersIdsYes);
                                            usersIds.put (ValAnswer.users_no.name () , usersIdsNo);

                                            answer.put (ValAnswer.users.name () , usersIds.toString ());
                                            l.n (ToJson.To (request) , EventName.ssg_get_all_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , null , usersIds.toString () , SubmitRequestType.socket , true);
                                        }
                                        else
                                        {
                                            /*
                                             * khata darim chon bala ma tedad answer ha ro gereftim agar inja biyad moshkeli bish omade
                                             */
                                            answer = AnswerToClient.ServerError ();
                                            l.n (ToJson.To (request) , EventName.ssg_get_all_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , ValAnswer.not_answer.name () , SubmitRequestType.socket , true);
                                        }
                                    }
                                    else
                                    {
                                        answer = AnswerToClient.IdInvalid (ValAnswer.not_answer.name ());
                                        l.n (ToJson.To (request) , EventName.ssg_get_all_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_answer.name ()) , null , SubmitRequestType.socket , true);
                                    }
                                }
                                else
                                {
                                    /*
                                     * question yes no nabode
                                     */
                                    final List <QuestionTextOptions> options = questionText.getOptions ();

                                    if (options != null && options.size () > 0)
                                    {
                                        try
                                        {
                                            final JSONObject answers = new JSONObject ();

                                            /**
                                             * tak tak option ha pasokhha ro migiram
                                             *
                                             * yani pasokhmarbot be har option
                                             *
                                             * @see AnswerQuestionsTextService#answersOptions(long , long)
                                             */
                                            for (final QuestionTextOptions option : options)
                                            {
                                                final JSONArray usersId = new JSONArray ();
                                                final AnswersOption answersOption = answerQuestionsTextService.answersOptions (questionTextId.getId () , option.getId ());

                                                /*
                                                 * barasi ke pasokh vojod dashte ya na
                                                 */
                                                if (answersOption != null)
                                                {
                                                    /**
                                                     * karbarani ke gerfte shode ro baraye barasi sath dastrasi tavasot darkhast konande barasi mishan
                                                     *
                                                     * @see UserProfileAccessLevel
                                                     * @see UserProfileAccessLevel#hasAccess(Which)
                                                     */
                                                    for (final MainAccount mainAccount : answersOption.mainAccount)
                                                    {
                                                        accessLevel.setUser (mainAccount);

                                                        if (accessLevel.hasAccess (Which.find_me , Which.id))
                                                        {
                                                            /*
                                                             * inja sath dartrasi vojod dare va daron JSONArray id karbar rikhte mishe
                                                             */
                                                            usersId.put (mainAccount.getId ());
                                                        }
                                                        else
                                                        {
                                                            /*
                                                             * inja dastersi vojod nadashte va ye log sabt mishe
                                                             */
                                                            l.n (Thread.currentThread ().getStackTrace () , new Exception ("access denied") , accessLevel.toString ());
                                                        }
                                                    }
                                                    if (usersId.length () > 0)
                                                        answers.put (String.valueOf (option.getIndex ()) , usersId);
                                                }
                                            }

                                            if (answers.length () > 0)
                                            {
                                                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());
                                                answer.put (ValAnswer.users.name () , answers.toString ());
                                                l.n (ToJson.To (request) , EventName.ssg_get_all_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , null , ToJson.CreateClass.nj ("question_text_id" , questionText.getId ()) , SubmitRequestType.socket , false);
                                            }
                                            else
                                            {
                                                answer = AnswerToClient.IdInvalid (ValAnswer.not_answer.name ());
                                                l.n (ToJson.To (request) , EventName.ssg_get_all_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_answer.name ()) , ToJson.CreateClass.nj ("question_text_id" , questionText.getId ()) , SubmitRequestType.socket , true);
                                            }
                                        }
                                        catch (JSONException e)
                                        {
                                            answer = AnswerToClient.ServerError ();
                                            l.n (ToJson.To (request) , EventName.ssg_get_all_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , e , ToJson.CreateClass.nj ("question_text_id" , questionText.getId ()) , SubmitRequestType.socket , true);
                                        }
                                    }
                                    else
                                    {
                                        /*
                                         * agar (options == null && options.size () == 0) hatman moshkeli pish amade chon vaghti be in ghesmat miyad question option hast
                                         */
                                        answer = AnswerToClient.ServerError ();
                                        l.n (ToJson.To (request) , EventName.ssg_get_all_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , ToJson.CreateClass.nj ("question_text_id" , questionText.getId ()) , SubmitRequestType.socket , true);
                                    }
                                }
                            }
                            else
                            {
                                answer = AnswerToClient.IdInvalid (ValAnswer.not_found_question_text.name ());
                                l.n (ToJson.To (request) , EventName.ssg_get_all_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_found_question_text.name ()) , null , SubmitRequestType.socket , true);
                            }
                        }
                        else
                        {
                            answer = AnswerToClient.IdInvalid (ValAnswer.not_found_gap.name ());
                            l.n (ToJson.To (request) , EventName.ssg_get_all_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_found_gap.name ()) , null , SubmitRequestType.socket , true);
                        }
                    }
                    else
                    {
                        answer = AnswerToClient.IdInvalid (ValAnswer.invalid_personal_gap_id.name ());
                        l.n (ToJson.To (request) , EventName.ssg_get_all_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_personal_gap_id.name ()) , null , SubmitRequestType.socket , true);
                    }
                }
                else
                {
                    answer = AnswerToClient.IdInvalid (ValAnswer.invalid_gap_id.name ());
                    l.n (ToJson.To (request) , EventName.ssg_get_all_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_gap_id.name ()) , null , SubmitRequestType.socket , true);
                }
            }
            else
            {
                answer = AnswerToClient.IdInvalid (ValAnswer.invalid_question_text_id.name ());
                l.n (ToJson.To (request) , EventName.ssg_get_all_answer_question_text.name () , client.getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_question_text_id.name ()) , null , SubmitRequestType.socket , true);
            }

            final String answerToJson = ToJson.To (answer);
            client.online.getClient ().sendEvent (EventName.e_ssg_get_all_answer_question_text.name () , answerToJson);
            l.n (Thread.currentThread ().getStackTrace () , ToJson.CreateClass.n ("send_to_client" , answerToJson).put ("user" , client.getMainAccount ().getId ()).put (l.SEND_TO_CLIENT , true).toJson ());
        }
    }

    private enum ValAnswer
    {
        invalid_question_text_id, invalid_gap_id, invalid_personal_gap_id,
        not_found_gap, not_found_question_text, not_answer, users, users_yes, users_no,
    }
}