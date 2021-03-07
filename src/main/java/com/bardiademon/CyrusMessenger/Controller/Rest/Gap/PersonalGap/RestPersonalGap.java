package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.PersonalGap;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Rest.UsedRequests.R_IDUsername;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserGapAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultKey;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.Pagination;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNGap.PERSONAL_GAP, method = RequestMethod.POST)
public final class RestPersonalGap
{
    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;
    private final PersonalGapsService personalGapsService;
    private final DefaultService defaultService;
    private final GapsService gapsService;

    // c => Create
    private final String cRouter;
    private final SubmitRequestType cType;

    // gpg => Get Personal Gap
    private final String gpgRouter;
    private final SubmitRequestType gpgType;

    // d => Delete
    private final String dRouter;
    private final SubmitRequestType dType;

    private final Pagination pagination;

    @Autowired
    public RestPersonalGap
            (final UserLoginService _UserLoginService ,
             final MainAccountService _MainAccountService ,
             final PersonalGapsService _PersonalGapsService ,
             final DefaultService _DefaultService ,
             final GapsService _GapsService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.personalGapsService = _PersonalGapsService;
        this.defaultService = _DefaultService;
        this.gapsService = _GapsService;

        this.cRouter = Domain.RNGap.PERSONAL_GAP + "/create";
        this.cType = SubmitRequestType.create_personal_gap;

        this.gpgRouter = Domain.RNGap.PERSONAL_GAP + "/";
        this.gpgType = SubmitRequestType.get_personal_gap;

        this.dRouter = Domain.RNGap.PERSONAL_GAP + "/delete";
        this.dType = SubmitRequestType.delete_personal_gap;

        this.pagination = new Pagination ();
    }

    @RequestMapping (value = "/create")
    public AnswerToClient create
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody R_IDUsername request)
    {
        /*
         *  CreatedBy => kasi ke login shode , GapWith => mishe hamin request ke kasi ke login shode id ya username ro mifreste
         */

        AnswerToClient answer;

        final CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , cRouter , cType);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            final MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            if (request != null)
            {
                final String strRequest = ToJson.To (request);

                final IdUsernameMainAccount idUsernameMainAccount = new IdUsernameMainAccount (mainAccountService , request.getId () , request.getUsername ());
                if (idUsernameMainAccount.isValid ())
                {
                    PersonalGaps personalGap = personalGapsService.getPersonalGap (mainAccount.getId () , idUsernameMainAccount.getIdUser ());
                    if (personalGap == null)
                    {
                        if ((new UserProfileAccessLevel (mainAccount , idUsernameMainAccount.getMainAccount ())).hasAccess (Which.find_me , ((request.getId () > 0)) ? Which.id : Which.username))
                        {
                            if ((new UserGapAccessLevel (mainAccount , idUsernameMainAccount.getMainAccount ())).hasAccess (Which.s_message))
                            {
                                personalGap = new PersonalGaps ();
                                personalGap.setLastIndex (0);
                                personalGap.setCreatedBy (mainAccount);
                                personalGap.setGapWith (idUsernameMainAccount.getMainAccount ());

                                personalGap = personalGapsService.Repository.save (personalGap);
                                if (personalGap.getId () > 0)
                                {
                                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.created);
                                    answer.put (AnswerToClient.CUK.id , personalGap.getId ());
                                    answer.setReqRes (req , res);
                                    l.n (strRequest , cRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.please_try_again) , cType , false);
                                }
                                else
                                {
                                    answer = AnswerToClient.ServerError ();
                                    answer.setReqRes (req , res);
                                    l.n (strRequest , cRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.please_try_again) , cType , true);
                                }
                            }
                            else
                            {
                                answer = AnswerToClient.AccessDenied ();
                                answer.setReqRes (req , res);
                                l.n (strRequest , cRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.access_denied) , cType , true);
                            }
                        }
                        else
                        {
                            answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , AnswerToClient.CUV.user_not_found);
                            answer.setReqRes (req , res);
                            l.n (strRequest , cRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.user_not_found) , "UserProfileAccessLevel => !Which.find_me || !((request.getId () > 0)) ? Which.id : Which.username)" , cType , true);
                        }
                    }
                    else
                    {
                        answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found);
                        answer.put (AnswerToClient.CUK.id , personalGap.getId ());
                        answer.setReqRes (req , res);
                        l.n (strRequest , cRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (IdUsernameMainAccount.class.getName ()) , cType , true);
                    }
                }
                else
                {
                    answer = idUsernameMainAccount.getAnswerToClient ();
                    answer.setReqRes (req , res);
                    l.n (strRequest , cRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (IdUsernameMainAccount.class.getName ()) , cType , true);
                }
            }
            else
            {
                answer = AnswerToClient.RequestIsNull ();
                answer.setReqRes (req , res);
                l.n (null , cRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.request_is_null) , cType , true);
            }
        }
        else answer = both.getAnswerToClient ();

        return answer;
    }

    @RequestMapping (value = { "/" , "/{last_get}" })
    public AnswerToClient getPersonalGaps
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @PathVariable (value = "last_get", required = false) String strLastPage)
    {
        AnswerToClient answer;

        String request = ToJson.CreateClass.nj ("last_get" , strLastPage);

        final CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , gpgRouter , gpgType);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            final MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            // chone shaih be id hast baraye validation ba class id in caro mikonam
            ID idLastPage = new ID (strLastPage);

            if (idLastPage.isValid ())
            {
                int thisPage = (int) idLastPage.getId ();
                if (idLastPage.getId () > 1) thisPage++;

                final DefaultService.Value <Integer> maxGet = defaultService.integerValue (DefaultKey.max_get_personal_gaps);

                if (maxGet.ok)
                {
                    long countPersonalGaps = personalGapsService.getCountPersonalGaps (mainAccount.getId ());

                    Pagination.Answer answerPagination = pagination.computing (thisPage , countPersonalGaps , maxGet.value);

                    if (answerPagination.Start >= 0 && answerPagination.End >= 0)
                    {
                        final List <PersonalGaps> personalGaps = personalGapsService.getPersonalGaps (mainAccount.getId () , answerPagination.Start , answerPagination.End);

                        if (personalGaps != null && personalGaps.size () > 0)
                        {

                            final List <AnswerGetPersonalGaps> madeByYou = new ArrayList <> ();
                            final List <AnswerGetPersonalGaps> gapWithYou = new ArrayList <> ();

                            final UserProfileAccessLevel accessLevel = new UserProfileAccessLevel (mainAccount);

                            int counter = 0;
                            for (final PersonalGaps personalGap : personalGaps)
                            {
                                final R_IDUsername idUsername = new R_IDUsername ();

                                if (personalGap.getCreatedBy ().getId () == mainAccount.getId ())
                                    accessLevel.setUser (personalGap.getGapWith ());
                                else
                                    accessLevel.setUser (personalGap.getCreatedBy ());

                                if (accessLevel.hasAccess (Which.id))
                                    idUsername.setId (accessLevel.getUser ().getId ());
                                else if (accessLevel.hasAccess (Which.username))
                                    idUsername.setUsername (accessLevel.getUser ().getUsername ().getUsername ());
                                else
                                    idUsername.setUsername (AnswerToClient.CUV.anonymous.name ().substring (0 , 1).toUpperCase ());

                                final AnswerGetPersonalGaps answerPersonalGap = new AnswerGetPersonalGaps ();
                                answerPersonalGap.setIdUsername (idUsername);
                                answerPersonalGap.setPersonalGapId (personalGap.getId ());

                                answerPersonalGap.setIndex (counter++);

                                if (personalGap.getGapWith ().getId () == mainAccount.getId ())
                                    gapWithYou.add (answerPersonalGap);
                                else madeByYou.add (answerPersonalGap);
                            }

                            answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found);
                            answer.put (KeyAnswer.made_by_you , madeByYou);
                            answer.put (KeyAnswer.gaw_with_you , gapWithYou);
                            answer.put (KeyAnswer.all_page , answerPagination.AllPage);
                            answer.put (KeyAnswer.this_page , thisPage);
                            answer.setReqRes (req , res);
                            l.n (request , gpgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found) , gpgType , false);
                        }
                        else
                        {
                            answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found);
                            answer.setReqRes (req , res);
                            l.n (request , gpgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found) , gpgType , true);
                        }
                    }
                    else
                    {
                        answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found);
                        answer.setReqRes (req , res);
                        l.n (request , gpgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found) , String.valueOf (thisPage) , gpgType , true);
                    }
                }
                else
                {
                    answer = maxGet.answer;
                    answer.setReqRes (req , res);
                    l.n (request , gpgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.please_try_again) , DefaultKey.max_get_personal_gaps.name () , gpgType , true);
                }
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.invalid_last_page);
                answer.setReqRes (req , res);
                l.n (request , gpgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.invalid_last_page) , gpgType , true);
            }


        }
        else answer = both.getAnswerToClient ();

        return answer;
    }

    @RequestMapping (value = { "/delete" , "/delete/{personal_gap_id}" , "/delete/{personal_gap_id}/{delete_both}" })
    private AnswerToClient delete
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @PathVariable (value = "personal_gap_id", required = false) String strPersonalGapId ,
             @PathVariable (value = "delete_both", required = false) String strDeleteBoth)
    {
        AnswerToClient answer;

        final String request = ToJson.CreateClass.n ("personal_gap_id" , strPersonalGapId).put ("delete_both" , strDeleteBoth).toJson ();

        final CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , dRouter , dType);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            final MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            final ID personalGapId = new ID (strPersonalGapId);
            if (personalGapId.isValid ())
            {
                if (Str.IsEmpty (strDeleteBoth)) strDeleteBoth = "false";

                if (Str.HasBool (strDeleteBoth))
                {
                    final PersonalGaps personalGaps = personalGapsService.byId (personalGapId.getId () , mainAccount.getId ());
                    if (personalGaps != null)
                    {
                        Boolean deleteBoth = Str.ToBool (strDeleteBoth);
                        assert deleteBoth != null;

                        if (personalGaps.getGapWith ().getId () == mainAccount.getId ()
                                && personalGaps.getCreatedBy ().getId () == mainAccount.getId ())
                            deleteBoth = true;

                        if (mainAccount.getId () == personalGaps.getCreatedBy ().getId ())
                        {
                            personalGaps.setDeletedByCreatedBy (true);
                            personalGaps.setDeletedAt_CreatedBy (Time.now ());

                            if (deleteBoth)
                            {
                                personalGaps.setDeletedForGapWith (true);
                                personalGaps.setDeletedAt_GapWith (Time.now ());

                                This.getServer ().getServerSocketGap ().deletePersonalGap (personalGaps.getGapWith () , personalGaps.getId ());
                            }
                        }
                        else
                        {
                            personalGaps.setDeletedForGapWith (true);
                            personalGaps.setDeletedAt_GapWith (Time.now ());

                            if (deleteBoth)
                            {
                                personalGaps.setDeletedByCreatedBy (true);
                                personalGaps.setDeletedAt_CreatedBy (Time.now ());

                                This.getServer ().getServerSocketGap ().deletePersonalGap (personalGaps.getCreatedBy () , personalGaps.getId ());
                            }
                        }
                        if (deleteBoth)
                        {
                            personalGaps.setDeleteBothBy (mainAccount);
                            gapsService.deleteBoth (personalGaps , mainAccount);
                        }

                        personalGapsService.Repository.save (personalGaps);

                        answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.removed);
                        answer.setReqRes (req , res);
                        l.n (request , dRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.removed) , dType , true);
                    }
                    else
                    {
                        answer = AnswerToClient.IdInvalid (AnswerToClient.CUV.not_found);
                        answer.setReqRes (req , res);
                        l.n (request , dRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found) , strPersonalGapId , dType , true);
                    }
                }
                else
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_delete_both);
                    answer.setReqRes (req , res);
                    l.n (request , dRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.invalid_delete_both) , strDeleteBoth , dType , true);
                }
            }
            else
            {
                answer = AnswerToClient.IdInvalid ();
                answer.setReqRes (req , res);
                l.n (request , dRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found) , strPersonalGapId , dType , true);
            }
        }
        else answer = both.getAnswerToClient ();

        return answer;
    }

    private final static class AnswerGetPersonalGaps
    {
        @JsonProperty ("personal_gap_id")
        private long personalGapId;

        @JsonProperty ("gap_with")
        private R_IDUsername idUsername;

        private int index;

        public AnswerGetPersonalGaps ()
        {
        }

        public long getPersonalGapId ()
        {
            return personalGapId;
        }

        public void setPersonalGapId (long personalGapId)
        {
            this.personalGapId = personalGapId;
        }

        public R_IDUsername getIdUsername ()
        {
            return idUsername;
        }

        public void setIdUsername (R_IDUsername idUsername)
        {
            this.idUsername = idUsername;
        }

        public int getIndex ()
        {
            return index;
        }

        public void setIndex (int index)
        {
            this.index = index;
        }
    }

    private enum ValAnswer
    {
        created, invalid_last_page, invalid_delete_both
    }

    private enum KeyAnswer
    {
        made_by_you, gaw_with_you, all_page, this_page
    }
}
