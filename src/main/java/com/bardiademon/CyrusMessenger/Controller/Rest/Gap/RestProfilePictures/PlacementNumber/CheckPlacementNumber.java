package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.PlacementNumber;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicFor;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public final class CheckPlacementNumber
{
    private final ProfilePicturesService profilePicturesService;
    private final MainAccount mainAccount;
    private final String router;
    private final SubmitRequestType type;
    private final TypeId typeId;
    private final long id;
    private final HttpServletResponse res;
    private final HttpServletRequest req;
    private final List <PlacementNumber> placementNumbers;

    private Answer answer;

    public CheckPlacementNumber
            (ProfilePicturesService _ProfilePicturesService , MainAccount _MainAccount ,
             String Router , SubmitRequestType Type , long Id , TypeId _TypeId ,
             HttpServletResponse Res , HttpServletRequest Req , List <PlacementNumber> _PlacementNumbers)
    {
        this.profilePicturesService = _ProfilePicturesService;
        this.mainAccount = _MainAccount;
        this.router = Router;
        this.type = Type;
        this.typeId = _TypeId;
        this.id = Id;
        this.res = Res;
        this.req = Req;
        this.placementNumbers = _PlacementNumbers;
        check ();
    }

    private void check ()
    {
        List <PlacementNumberError> errors = new ArrayList <> ();

        int numberMain = 0;

        AnswerToClient answerToClient;

        for (PlacementNumber placementNumber : placementNumbers)
        {
            if (placementNumber.isMain ()) numberMain++;

            if (numberMain >= 2)
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ErrAnswer.just_one_main);
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (placementNumbers) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ErrAnswer.just_one_main));
                r.n (mainAccount , type , true);

                answer = new Answer (answerToClient , null);
                return;
            }
        }

        if (numberMain > 0)
        {
            switch (typeId)
            {
                case group:
                    profilePicturesService.Repository.disableMainPhotoGroup (id , ProfilePicFor.group);
                    break;
                case user:
                    profilePicturesService.Repository.disableMainPhotoUser (id , ProfilePicFor.user);
                    break;
            }
        }

        answer = new Answer (null , new ArrayList <> ());

        ID id;
        List <Long> ids = new ArrayList <> ();

        ProfilePictures profilePicture = null;
        for (PlacementNumber placementNumber : placementNumbers)
        {
            if ((id = new ID (placementNumber.getId ())).isValid ())
            {
                if (!ids.contains (id.getId ()))
                {
                    ids.add (id.getId ());

                    switch (typeId)
                    {
                        case group:
                            profilePicture = profilePicturesService.getOneGroup (id.getId () , this.id);
                            break;
                        case user:
                            profilePicture = profilePicturesService.getOneForUser (id.getId () , this.id);
                            break;
                    }

                    if (profilePicture != null) answer.profilePictures.add (profilePicture);
                    else errors.add (new PlacementNumberError (placementNumber , ErrAnswer.id_not_found));
                }
                else errors.add (new PlacementNumberError (placementNumber , ErrAnswer.duplicate_id));
            }
            else errors.add (new PlacementNumberError (placementNumber , ErrAnswer.id_invalid));
        }

        if (errors.size () > 0)
        {
            answer.answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , AnswerToClient.CUV.error);
            answer.answerToClient.put (KeyAnswer.result , errors);
            answer.answerToClient.setReqRes (req , res);
            l.n (ToJson.To (placementNumbers) , router , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , l.e (ErrAnswer.just_one_main));
            r.n (mainAccount , type , true);
        }
    }

    public static class PlacementNumberError extends PlacementNumber
    {
        private final String error;

        public PlacementNumberError (final PlacementNumber _PlacementNumber , final Enum <?> Error)
        {
            this (_PlacementNumber , Error.name ());
        }

        public PlacementNumberError (PlacementNumber _PlacementNumber , String Error)
        {
            super.setId (_PlacementNumber.getId ());
            super.setMain (_PlacementNumber.isMain ());
            super.setPlacementNumber (_PlacementNumber.getPlacementNumber ());
            super.setUpdateMain (_PlacementNumber.isUpdateMain ());
            this.error = Error;
        }

        public String getError ()
        {
            return error;
        }
    }

    public static class Answer
    {
        private AnswerToClient answerToClient;
        private final List <ProfilePictures> profilePictures;

        public Answer (AnswerToClient answerToClient , List <ProfilePictures> profilePictures)
        {
            this.answerToClient = answerToClient;
            this.profilePictures = profilePictures;
        }

        public AnswerToClient getAnswerToClient ()
        {
            return answerToClient;
        }

        public List <ProfilePictures> getProfilePictures ()
        {
            return profilePictures;
        }
    }

    private enum ErrAnswer
    {
        id_invalid, id_not_found, duplicate_id, just_one_main
    }

    public enum TypeId
    {
        user, group
    }

    private enum KeyAnswer
    {
        result
    }

    public Answer getAnswer ()
    {
        return answer;
    }

    public boolean isOk ()
    {
        return (answer.answerToClient == null);
    }
}
