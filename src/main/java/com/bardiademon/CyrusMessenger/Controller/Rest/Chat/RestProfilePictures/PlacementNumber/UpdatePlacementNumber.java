package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.PlacementNumber;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.ProfilePictures.SortProfilePictures;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public final class UpdatePlacementNumber
{
    private AnswerToClient answerToClient;

    private final List<ProfilePictures> profilePictures;
    private final List<PlacementNumber> placementNumbers;
    private Object request;
    private final ProfilePicturesService profilePicturesService;
    private final MainAccount mainAccount;
    private final String router;
    private final SubmitRequestType type;
    private final HttpServletResponse res;
    private final HttpServletRequest req;

    public UpdatePlacementNumber
            (List<ProfilePictures> _ProfilePictures , List<PlacementNumber> _PlacementNumbers ,
             ProfilePicturesService _ProfilePicturesService , MainAccount _MainAccount ,
             String Router , SubmitRequestType Type ,
             HttpServletResponse Res , HttpServletRequest Req , Object Request)
    {
        this.profilePictures = _ProfilePictures;
        this.profilePicturesService = _ProfilePicturesService;
        this.mainAccount = _MainAccount;
        this.router = Router;
        this.type = Type;
        this.res = Res;
        this.req = Req;
        this.placementNumbers = _PlacementNumbers;
        this.request = Request;
        check ();
    }

    public void check ()
    {
        ProfilePictures profilePicture;
        PlacementNumber placementNumber;
        for (int i = 0, len = profilePictures.size (); i < len; i++)
        {
            profilePicture = profilePictures.get (i);
            placementNumber = placementNumbers.get (i);
            if (placementNumber.getPlacementNumber () < 0) placementNumber.setPlacementNumber (0);

            profilePicture.setPlacementNumber (placementNumber.getPlacementNumber ());
            if (placementNumber.isUpdateMain ())
                profilePicture.setMainPic (placementNumber.isMain ());
        }

        new SortProfilePictures (profilePicturesService , profilePictures);

        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.updated.name ());
        answerToClient.setReqRes (req , res);
        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.updated.name ());
        r.n (mainAccount , type , false);
    }

    public AnswerToClient getAnswerToClient ()
    {
        return answerToClient;
    }

    private enum ValAnswer
    {
        updated
    }
}
