package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Upload;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicFor;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;

import javax.servlet.http.HttpServletResponse;

public final class AccessUploadProfilePicture
{
    private Service service;
    private String codeLogin;
    private ProfilePicFor profilePicFor;

    private CheckLogin checkLogin;
    private boolean hasAccess;
    private AnswerToClient answerToClient;

    private boolean checkMaxUpload;

    public AccessUploadProfilePicture
            (Service _Service , String CodeLogin , ProfilePicFor _ProfilePicFor)
    {
        this (_Service , CodeLogin , _ProfilePicFor , true);
    }

    public AccessUploadProfilePicture
            (Service _Service , String CodeLogin , ProfilePicFor _ProfilePicFor , boolean CheckMaxUpload)
    {
        this.service = _Service;
        this.codeLogin = CodeLogin;
        this.profilePicFor = _ProfilePicFor;
        this.checkMaxUpload = CheckMaxUpload;
        hasAccess = check ();
    }

    private boolean check ()
    {
        if (checkLogin ())
        {
            switch (profilePicFor)
            {
                case user:
                    if (checkMaxUpload) return checkMaxUploadUser ();
                case group:
                case channel:
                default:
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.ServerError () , "Just user");
            }
        }
        return false;
    }

    private boolean checkMaxUploadUser ()
    {
        int maxUploadProfilePictures = service.securityUserProfileService.Repository.getMaxUploadProfilePictures (checkLogin.getVCodeLogin ().getMainAccount ().getId ());
        int countUploaded = service.profilePicturesService.countUploadPic (profilePicFor);
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

        public Service (UserLoginService _UserLoginService , SecurityUserProfileService securityUserProfileService , ProfilePicturesService _ProfilePicturesService)
        {
            this.userLoginService = _UserLoginService;
            this.securityUserProfileService = securityUserProfileService;
            this.profilePicturesService = _ProfilePicturesService;
        }

        public UserLoginService getUserLoginService ()
        {
            return userLoginService;
        }

        public SecurityUserProfileService getSecurityUserProfileService ()
        {
            return securityUserProfileService;
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

    private enum ValAnswer
    {
        photo_upload_limit_is_filled
    }
}
