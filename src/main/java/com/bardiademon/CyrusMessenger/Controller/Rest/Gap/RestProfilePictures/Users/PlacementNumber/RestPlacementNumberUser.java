package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.Users.PlacementNumber;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.PlacementNumber.CheckPlacementNumber;
import com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.PlacementNumber.PlacementNumber;
import com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.PlacementNumber.UpdatePlacementNumber;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping (value = Domain.RNGap.RNProfilePicture.RN_PP_PLACEMENT_NUMBER_USER, method = RequestMethod.POST)
public final class RestPlacementNumberUser
{
    private final UserLoginService userLoginService;
    private final ProfilePicturesService profilePicturesService;

    @Autowired
    public RestPlacementNumberUser (UserLoginService _UserLoginService , ProfilePicturesService _ProfilePicturesService)
    {
        this.userLoginService = _UserLoginService;
        this.profilePicturesService = _ProfilePicturesService;
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient update
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @RequestBody List <PlacementNumber> request)
    {
        AnswerToClient answerToClient;

        String router = Domain.RNGap.RNProfilePicture.RN_PP_PLACEMENT_NUMBER_USER;
        SubmitRequestType type = SubmitRequestType.placement_number_user;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null && request.size () > 0)
            {
                CheckPlacementNumber checkPlacementNumber = new CheckPlacementNumber (profilePicturesService , mainAccount , router , type , mainAccount.getId () , CheckPlacementNumber.TypeId.user , res , req , request);
                if (checkPlacementNumber.isOk ())
                    answerToClient = (new UpdatePlacementNumber (checkPlacementNumber.getAnswer ().getProfilePictures () , request , profilePicturesService , mainAccount , router , type , res , req , request)).getAnswerToClient ();
                else answerToClient = checkPlacementNumber.getAnswer ().getAnswerToClient ();
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.request_is_null));
                r.n (mainAccount , type , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

}
