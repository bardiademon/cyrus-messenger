package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Upload;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicFor;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;

import javax.servlet.http.HttpServletResponse;

public class AccessUploadProfilePicture
{
    protected Service service;
    protected String codeLogin;

    protected CheckLogin checkLogin;
    protected boolean hasAccess;
    protected AnswerToClient answerToClient;

    protected long id;
    protected boolean checkMaxUpload;

    public AccessUploadProfilePicture
            (Service _Service , String CodeLogin , boolean CheckMaxUpload)
    {
        this.service = _Service;
        this.codeLogin = CodeLogin;
        this.checkMaxUpload = CheckMaxUpload;
        hasAccess = check ();
    }

    private boolean check ()
    {
        if (checkLogin ())
        {
            if (checkMaxUpload) return checkMaxUploadUser ();
            else return true;
        }
        return false;
    }

    private boolean checkMaxUploadUser ()
    {
        int maxUploadProfilePictures = service.securityUserProfileService.Repository.getMaxUploadProfilePictures (checkLogin.getVCodeLogin ().getMainAccount ().getId ());
        int countUploaded = service.profilePicturesService.countUploadPic (ProfilePicFor.user);

        if ((countUploaded != 0 && maxUploadProfilePictures != 0) && countUploaded >= maxUploadProfilePictures)
        {
            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_NOT_ACCEPTABLE) , ValAnswer.photo_upload_limit_is_filled.name ());
            return false;
        }
        else return true;
    }

    private boolean checkLogin ()
    {
        checkLogin = new CheckLogin (codeLogin , service.userLoginService.Repository);
        if (!checkLogin.isValid ())
        {
            answerToClient = checkLogin.getAnswerToClient ();
            return false;
        }
        else return true;
    }

    public static class Service
    {
        private final UserLoginService userLoginService;
        private final SecurityUserProfileService securityUserProfileService;
        private final ProfilePicturesService profilePicturesService;

        public Service (UserLoginService _UserLoginService , SecurityUserProfileService _SecurityUserProfileService , ProfilePicturesService _ProfilePicturesService )
        {
            this.userLoginService = _UserLoginService;
            this.securityUserProfileService = _SecurityUserProfileService;
            this.profilePicturesService = _ProfilePicturesService;
        }

        public UserLoginService getUserLoginService ()
        {
            return userLoginService;
        }


        public ProfilePicturesService getProfilePicturesService ()
        {
            return profilePicturesService;
        }

    }

    public boolean hasAccess ()
    {
        return hasAccess;
    }

    public CheckLogin getCheckLogin ()
    {
        return checkLogin;
    }

    public AnswerToClient getAnswerToClient ()
    {
        return answerToClient;
    }


    protected enum ValAnswer
    {
        photo_upload_limit_is_filled
    }
}
