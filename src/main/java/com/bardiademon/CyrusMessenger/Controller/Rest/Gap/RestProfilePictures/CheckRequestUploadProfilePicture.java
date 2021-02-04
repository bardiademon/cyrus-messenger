package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.Users.Upload.RequestUploadProfilePictureUser;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.bardiademon.Default.DSize;
import com.bardiademon.CyrusMessenger.bardiademon.GetSize;
import com.bardiademon.CyrusMessenger.bardiademon.IO.CheckImage;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckRequestUploadProfilePicture
{
    private final RequestUploadProfilePictureUser request;
    private final MainAccount mainAccount;
    private final HttpServletResponse res;
    private final HttpServletRequest req;
    private final String router;
    private final SubmitRequestType submitRequestType;

    private AnswerToClient answerToClient = null;

    private CheckImage checkImage;

    public CheckRequestUploadProfilePicture
            (
                    RequestUploadProfilePictureUser Request ,
                    MainAccount _MainAccount ,
                    HttpServletResponse Res ,
                    HttpServletRequest Req ,
                    String Router ,
                    SubmitRequestType _SubmitRequestType
            )
    {
        this.request = Request;
        this.mainAccount = _MainAccount;
        this.res = Res;
        this.req = Req;
        this.router = Router;
        this.submitRequestType = _SubmitRequestType;
        if (checkRequest ()) checkSize ();
    }

    private boolean checkRequest ()
    {
        if (request != null)
        {
            if (request.getPicture () != null)
            {
                checkImage = new CheckImage ();
                if (checkImage.valid (request.getPicture ()))
                {
                    if (request.getPlacement_number () < -1)
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.placement_number_invalid.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.placement_number_invalid.name ()) , null);
                        r.n (mainAccount , submitRequestType , true);
                    }
                    else return true;
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.picture_invalid.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.picture_invalid.name ()) , null);
                    r.n (mainAccount , submitRequestType , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.picture_is_null.name ());
                answerToClient.setReqRes (req , res);
                l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.picture_is_null.name ()) , null);
                r.n (mainAccount , submitRequestType , true);
            }
        }
        else
        {
            answerToClient = AnswerToClient.RequestIsNull ();
            answerToClient.setReqRes (req , res);
            l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("null request") , null);
            r.n (mainAccount , submitRequestType , true);
        }

        return false;
    }

    private void checkSize ()
    {
        long size = request.getPicture ().getSize ();
        if (size > DSize.SIZE_PROFILE_PICTURE)
        {
            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.image_size_is_larger_than_allowed.name ());
            answerToClient.put (KeyAnswer.image_size.name () , GetSize.Get (size));
            answerToClient.put (KeyAnswer.allowed_size.name () , GetSize.Get (DSize.SIZE_PROFILE_PICTURE));
            answerToClient.put (KeyAnswer.extra_size.name () , GetSize.Get ((Math.abs ((size - DSize.SIZE_PROFILE_PICTURE)))));

            answerToClient.setReqRes (req , res);
            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.image_size_is_larger_than_allowed.name ()) , null);
            r.n (mainAccount , submitRequestType , true);
        }
    }

    private enum ValAnswer
    {
        picture_is_null, placement_number_invalid, picture_invalid, image_size_is_larger_than_allowed
    }

    private enum KeyAnswer
    {
        image_size, allowed_size, extra_size
    }

    public CheckImage getCheckImage ()
    {
        return checkImage;
    }

    public AnswerToClient getAnswerToClient ()
    {
        return answerToClient;
    }
}
