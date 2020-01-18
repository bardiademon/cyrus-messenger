package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Get.Security.Profile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
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
@RequestMapping (value = RouterName.RNInfoUser.RNSecurity.RN_PROFILE, method = RequestMethod.POST)
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
                                                  @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;
        CheckLogin checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
        if (checkLogin.isValid ())
        {
            if (requestProfile.thereIsAtLeastOneTrue ())
            {
                SecurityUserProfile securityUserProfile
                        = securityUserProfileService.Repository.findByMainAccount (checkLogin.getVCodeLogin ().getMainAccount ());
                if (securityUserProfile != null)
                {
                    answerToClient = AnswerToClient.OK ();
                    if (requestProfile.isSecBio ())
                        answerToClient.put (KeyAnswer.sec_bio.name () , securityUserProfile.getShowBio ().name ());

                    if (requestProfile.isSecCover ())
                        answerToClient.put (KeyAnswer.sec_cover.name () , securityUserProfile.getShowCover ().name ());

                    if (requestProfile.isSecShowInChannel ())
                        answerToClient.put (KeyAnswer.sec_show_in_channel.name () ,
                                securityUserProfile.getShowInChannel ().name ());

                    if (requestProfile.isSecShowInGroup ())
                        answerToClient.put (KeyAnswer.sec_show_in_group.name () ,
                                securityUserProfile.getShowInGroup ().name ());

                    if (requestProfile.isSecShowProfile ())
                        answerToClient.put (KeyAnswer.sec_show_profile.name () ,
                                securityUserProfile.getShowInProfile ().name ());

                    if (requestProfile.isSecShowInSearch ())
                        answerToClient.put (KeyAnswer.show_in_search.name () ,
                                securityUserProfile.getShowInSearch ().name ());

                    if (requestProfile.isSecLastSeen ())
                        answerToClient.put (KeyAnswer.sec_last_seen.name () ,
                                securityUserProfile.getShowLastSeen ().name ());

                    if (requestProfile.isSecMyLink ())
                        answerToClient.put (KeyAnswer.sec_mylink.name () ,
                                securityUserProfile.getShowMyLink ().name ());

                    if (requestProfile.isSecName ())
                        answerToClient.put (KeyAnswer.sec_name.name () ,
                                securityUserProfile.getShowName ().name ());

                    if (requestProfile.isSecPersonalInformation ())
                        answerToClient.put (KeyAnswer.sec_personal_information.name () ,
                                securityUserProfile.getShowPersonalInformation ().name ());

                    if (requestProfile.isSecPhone ())
                        answerToClient.put (KeyAnswer.sec_phone.name () ,
                                securityUserProfile.getShowPhone ().name ());

                    if (requestProfile.isSecSeenMessage ())
                        answerToClient.put (KeyAnswer.sec_seen_message.name () ,
                                securityUserProfile.getShowSeenMessage ().name ());

                    if (requestProfile.isSecUsername ())
                        answerToClient.put (KeyAnswer.sec_username.name () ,
                                securityUserProfile.getShowUsername ().name ());

                    if (requestProfile.isSecListFriends ())
                        answerToClient.put (KeyAnswer.sec_list_friends.name () ,
                                securityUserProfile.getListFriends ().name ());
                }
                else answerToClient = AnswerToClient.ServerError ();
            }
            else answerToClient = AnswerToClient.error400 ();
        }
        else answerToClient = checkLogin.getAnswerToClient ();

        return answerToClient;
    }

    private enum KeyAnswer
    {
        sec_cover, sec_bio, sec_show_in_channel, sec_show_in_group,
        sec_show_profile, show_in_search, sec_last_seen, sec_mylink, sec_name,
        sec_personal_information, sec_phone, sec_seen_message, sec_username, sec_list_friends
    }

}
