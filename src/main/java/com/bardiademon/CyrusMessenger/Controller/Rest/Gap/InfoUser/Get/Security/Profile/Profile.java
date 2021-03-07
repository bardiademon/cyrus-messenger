package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Get.Security.Profile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;


@RestController
@RequestMapping (value = Domain.RNGap.RNInfoUser.RNSecurity.RN_PROFILE, method = RequestMethod.POST)
public class Profile
{

    private final UserLoginService userLoginService;
    private final SecurityUserProfileService securityUserProfileService;

    @Autowired
    public Profile (UserLoginService _UserLoginService , SecurityUserProfileService _SecurityUserProfileService)
    {
        this.userLoginService = _UserLoginService;
        this.securityUserProfileService = _SecurityUserProfileService;
    }

    @RequestMapping (value = {"/" , ""})
    public AnswerToClient getInfoSecurityProfile (@RequestBody RequestProfile requestProfile ,
                                                  @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;
        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
        {
            if (requestProfile.thereIsAtLeastOneTrue ())
            {
                SecurityUserProfile securityUserProfile
                        = securityUserProfileService.Repository.findByMainAccount (isLogin.getVCodeLogin ().getMainAccount ());
                if (securityUserProfile != null)
                {
                    answerToClient = AnswerToClient.OK ();
                    if (requestProfile.isSecBio ())
                        answerToClient.put (KeyAnswer.sec_bio , securityUserProfile.getShowBio ());

                    if (requestProfile.isSecCover ())
                        answerToClient.put (KeyAnswer.sec_cover , securityUserProfile.getShowCover ());

                    if (requestProfile.isSecShowInChannel ())
                        answerToClient.put (KeyAnswer.sec_show_in_channel ,
                                securityUserProfile.getShowInChannel ());

                    if (requestProfile.isSecShowInGroup ())
                        answerToClient.put (KeyAnswer.sec_show_in_group ,
                                securityUserProfile.getShowInGroup ());

                    if (requestProfile.isSecShowProfile ())
                        answerToClient.put (KeyAnswer.sec_show_profile ,
                                securityUserProfile.getShowInProfile ());

                    if (requestProfile.isSecShowInSearch ())
                        answerToClient.put (KeyAnswer.show_in_search ,
                                securityUserProfile.getShowInSearch ());

                    if (requestProfile.isSecLastSeen ())
                        answerToClient.put (KeyAnswer.sec_last_seen ,
                                securityUserProfile.getShowLastSeen ());

                    if (requestProfile.isSecMyLink ())
                        answerToClient.put (KeyAnswer.sec_mylink ,
                                securityUserProfile.getShowMyLink ());

                    if (requestProfile.isSecName ())
                        answerToClient.put (KeyAnswer.sec_name ,
                                securityUserProfile.getShowName ());

                    if (requestProfile.isSecFamily ())
                        answerToClient.put (KeyAnswer.sec_family ,
                                securityUserProfile.getShowFamily ());

                    if (requestProfile.isSecEmail ())
                        answerToClient.put (KeyAnswer.sec_email ,
                                securityUserProfile.getShowEmail ());

                    if (requestProfile.isSecPersonalInformation ())
                        answerToClient.put (KeyAnswer.sec_personal_information ,
                                securityUserProfile.getShowPersonalInformation ());

                    if (requestProfile.isSecPhone ())
                        answerToClient.put (KeyAnswer.sec_phone ,
                                securityUserProfile.getShowPhone ());

                    if (requestProfile.isSecSeenMessage ())
                        answerToClient.put (KeyAnswer.sec_seen_message ,
                                securityUserProfile.getShowSeenMessage ());

                    if (requestProfile.isSecUsername ())
                        answerToClient.put (KeyAnswer.sec_username ,
                                securityUserProfile.getShowUsername ());

                    if (requestProfile.isSecListFriends ())
                        answerToClient.put (KeyAnswer.sec_list_friends ,
                                securityUserProfile.getListFriends ());
                }
                else answerToClient = AnswerToClient.ServerError ();
            }
            else answerToClient = AnswerToClient.BadRequest ();
        }
        else answerToClient = isLogin.getAnswerToClient ();

        return answerToClient;
    }

    private enum KeyAnswer
    {
        sec_cover, sec_bio, sec_show_in_channel, sec_show_in_group,
        sec_show_profile, show_in_search, sec_last_seen, sec_mylink, sec_name, sec_family, sec_email,
        sec_personal_information, sec_phone, sec_seen_message, sec_username, sec_list_friends
    }

}
