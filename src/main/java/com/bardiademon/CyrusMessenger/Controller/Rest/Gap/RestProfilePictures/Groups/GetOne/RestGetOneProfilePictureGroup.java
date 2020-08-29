package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.Groups.GetOne;

import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.IsManager;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.ILUGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.IsJoined;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroupService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.ProfilePictures.SortProfilePictures;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.IO.ToByte;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping (value = Domain.RNGap.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE_GROUP, method = RequestMethod.POST)
public final class RestGetOneProfilePictureGroup
{
    private final UserLoginService userLoginService;
    private final ProfilePicturesService profilePicturesService;
    private final JoinGroupService joinGroupService;
    private final GroupsService groupsService;
    private final GroupManagementService groupManagementService;

    @Autowired
    public RestGetOneProfilePictureGroup
            (UserLoginService _UserLoginService ,
             ProfilePicturesService _ProfilePicturesService ,
             JoinGroupService _JoinGroupService , GroupsService _GroupsService , GroupManagementService _GroupManagementService)
    {
        this.userLoginService = _UserLoginService;
        this.profilePicturesService = _ProfilePicturesService;
        this.joinGroupService = _JoinGroupService;
        this.groupsService = _GroupsService;
        this.groupManagementService = _GroupManagementService;
    }

    @RequestMapping (value = { "/" , "" , "/{ID_PROFILE_PICTURE}" }, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getOne
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @PathVariable (value = "ID_PROFILE_PICTURE", required = false) String id)
    {
        String request = ToJson.CreateClass.n ("ID_PROFILE_PICTURE" , id).toJson ();
        String router = Domain.RNGap.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE_GROUP;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , SubmitRequestType.get_one_profile_picture_group);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            ID idProfilePicture = new ID (id);
            if (idProfilePicture.isValid ())
            {
                ProfilePictures profilePicture = profilePicturesService.Repository.findProfilePictureGroup (idProfilePicture.getId ());
                if (profilePicture != null)
                {
                    String pathProfilePicture = Path.StickTogether (Path.PROFILE_PICTURES_GROUPS ,
                            String.valueOf (profilePicture.getGroups ().getId ()) ,
                            String.format ("%s.%s" , profilePicture.getName () , profilePicture.getType ()));

                    ID idGroup = new ID (profilePicture.getGroups ().getId ());

                    if (!profilePicture.isMainPic ())
                    {
                        IsJoined isJoined = new IsJoined (joinGroupService , mainAccount , idGroup);
                        if (isJoined.is ())
                            return getByteProfilePicture (pathProfilePicture , mainAccount , router , request);
                        else
                        {
                            ILUGroup iluGroup = new ILUGroup (groupsService);
                            iluGroup.setId (idGroup.getId ());
                            iluGroup.isValid ();

                            IsManager isManager = new IsManager (mainAccount , groupManagementService);
                            isManager.setILUGroup (iluGroup);
                            if (isManager.isManager ())
                                return getByteProfilePicture (pathProfilePicture , mainAccount , router , request);
                            else
                            {
                                List <ProfilePictures> profilePictures = profilePicture.getGroups ().getProfilePictures ();
                                if (profilePictures != null)
                                {
                                    List <Long> ids = (new SortProfilePictures (profilePictures)).getIds ();
                                    assert ids != null;
                                    if (ids.get (0) == idProfilePicture.getId ())
                                        return getByteProfilePicture (pathProfilePicture , mainAccount , router , request);
                                    else return toByte (Path.GetImage (Path.IC_COVER_DEFAULT));
                                }
                                else return toByte (Path.GetImage (Path.IC_COVER_DEFAULT));
                            }
                        }
                    }
                    else return getByteProfilePicture (pathProfilePicture , mainAccount , router , request);
                }
                else
                {
                    l.n (request , router , mainAccount , null , Thread.currentThread ().getStackTrace () , new Exception ("profile_picture_not_found") , null);
                    return toByte (Path.GetImage (Path.IMAGE_ERROR_500));
                }
            }
            else
            {
                l.n (request , router , mainAccount , null , Thread.currentThread ().getStackTrace () , new Exception ("profile_picture_id_invalid") , null);
                return toByte (Path.GetImage (Path.IC_COVER_DEFAULT));
            }
        }
        else return toByte (Path.GetImage (Path.IC_COVER_DEFAULT));
    }

    private byte[] getByteProfilePicture (String pathProfilePicture , MainAccount mainAccount , String router , String request)
    {
        File file = new File (pathProfilePicture);
        if (file.exists ())
        {
            l.n (request , router , mainAccount , null , Thread.currentThread ().getStackTrace () , null , file.getPath ());
            return toByte (file.getPath ());
        }
        else
        {
            l.n (request , router , mainAccount , null , Thread.currentThread ().getStackTrace () , new Exception ("profile_picture_file_not_found") , file.getPath ());
            return toByte (Path.GetImage (Path.IMAGE_ERROR_500));
        }
    }

    private byte[] toByte (String path)
    {
        return ToByte.to (path);
    }
}
