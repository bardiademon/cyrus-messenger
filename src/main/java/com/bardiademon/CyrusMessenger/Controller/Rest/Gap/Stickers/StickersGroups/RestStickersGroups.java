package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Stickers.StickersGroups;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.HasStickerAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultKey;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultService;
import com.bardiademon.CyrusMessenger.Model.Database.DeletedOrEdited.DeletedOrEdited;
import com.bardiademon.CyrusMessenger.Model.Database.DeletedOrEdited.DeletedOrEditedService;
import com.bardiademon.CyrusMessenger.Model.Database.DeletedOrEdited.DeletedOrEditedType;
import com.bardiademon.CyrusMessenger.Model.Database.UploadedFiles.UploadedFiles;
import com.bardiademon.CyrusMessenger.Model.Database.UploadedFiles.UploadedFilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel.StickerAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel.StickerAccessLevelService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel.StickerAccessLevelType;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups.StickerGroups;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups.StickerGroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.ILUGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.GetSize;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.IO.CheckImage;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping (value = Domain.RNGap.STICKERS_GROUPS, method = RequestMethod.POST)
public final class RestStickersGroups
{

    private final StickerGroupsService stickerGroupsService;
    private final UserLoginService userLoginService;
    private final UploadedFilesService uploadedFilesService;
    private final DefaultService defaultService;
    private final UsernamesService usernamesService;
    private final GroupsService groupsService;
    private final DeletedOrEditedService deletedOrEditedService;

    /**
     * csg => Create Sticker Group
     */
    private final String csgRouter;
    private final SubmitRequestType csgType;

    /**
     * gi => Groups ids
     */
    private final String giRouter;
    private final SubmitRequestType giType;

    /**
     * g => Group
     */
    private final String gRouter;
    private final SubmitRequestType gType;

    /**
     * dg => Delete Group
     */
    private final String dgRouter;
    private final SubmitRequestType dgType;

    private final HasStickerAccessLevel hasStickerAccessLevel;

    @Autowired
    public RestStickersGroups (
            final StickerGroupsService _StickerGroupsService ,
            final UserLoginService _UserLoginService ,
            final UploadedFilesService _UploadedFilesService ,
            final DefaultService _DefaultService ,
            final StickerAccessLevelService _StickerAccessLevelService ,
            final UsernamesService _UsernamesService ,
            final GroupsService _GroupsService ,
            final DeletedOrEditedService _DeletedOrEditedService)
    {
        this.stickerGroupsService = _StickerGroupsService;
        this.userLoginService = _UserLoginService;
        this.uploadedFilesService = _UploadedFilesService;
        this.defaultService = _DefaultService;
        this.usernamesService = _UsernamesService;
        this.groupsService = _GroupsService;
        this.deletedOrEditedService = _DeletedOrEditedService;

        this.csgRouter = Domain.RNGap.STICKERS_GROUPS + "/create-sticker-group";
        this.csgType = SubmitRequestType.create_sticker_group;

        this.giRouter = csgRouter + "/groups-ids";
        this.giType = SubmitRequestType.get_groups_ids;

        this.gRouter = csgRouter + "/group";
        this.gType = SubmitRequestType.get_info_one_sticker_group;

        this.dgRouter = csgRouter + "/delete-group";
        this.dgType = SubmitRequestType.delete_sticker_group;

        hasStickerAccessLevel = new HasStickerAccessLevel (_StickerAccessLevelService);
    }

    @RequestMapping (value = "/create-sticker-group")
    public AnswerToClient createStickerGroup
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @ModelAttribute RequestCreateStickerGroup request)
    {
        AnswerToClient answer = null;

        String reqStr = ToJson.To (request);

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , csgRouter , csgType);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                boolean isUpdate = false, okId = true;
                ID idStickerGroups;
                StickerGroups stickerGroups = null;
                if (!Str.IsEmpty (request.getId ()))
                {
                    idStickerGroups = new ID (request.getId ());
                    if (!idStickerGroups.isValid ())
                    {
                        okId = false;
                        answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.invalid_sticker_group_id.name ());
                        answer.setReqRes (req , res);
                        l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_sticker_group_id.name ()) , ToJson.CreateClass.nj ("id" , request.getId ()) , csgType , true);
                    }
                    else
                    {
                        stickerGroups = stickerGroupsService.stickerGroups (idStickerGroups.getId () , mainAccount.getId ());
                        if (stickerGroups != null)
                            isUpdate = true;
                        else
                        {
                            okId = false;
                            answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.not_found_sticker_group_id.name ());
                            answer.setReqRes (req , res);
                            l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_found_sticker_group_id.name ()) , ToJson.CreateClass.nj ("id" , request.getId ()) , csgType , true);
                        }
                    }
                }

                if (okId)
                {
                    if (!Str.IsEmpty (request.getGroup_name ()))
                    {
                        MultipartFile groupImage = request.getGroup_image ();

                        boolean isNullGroupImage = groupImage == null;

                        System.out.println ((isUpdate || !isNullGroupImage));
                        if (isUpdate || !isNullGroupImage)
                        {
                            CheckImage checkImage = null;
                            if (isNullGroupImage || (checkImage = new CheckImage ()).valid (groupImage))
                            {
                                long imageSize = 0;
                                if (!isNullGroupImage) imageSize = groupImage.getSize ();

                                Long maxSizeSticker = defaultService.getLong (DefaultKey.max_size_sticker);
                                if (isNullGroupImage || maxSizeSticker != null && imageSize <= maxSizeSticker)
                                {
                                    Integer minWidth = 0, maxWidth = 0, minHeight = 0, maxHeight = 0;

                                    if (isNullGroupImage || (minWidth = defaultService.getInt (DefaultKey.min_w_sticker_image)) != null
                                            && (maxWidth = defaultService.getInt (DefaultKey.max_w_sticker_image)) != null
                                            && (minHeight = defaultService.getInt (DefaultKey.min_h_sticker_image)) != null
                                            && (maxHeight = defaultService.getInt (DefaultKey.max_h_sticker_image)) != null)
                                    {

                                        int imageWidth = 0;
                                        int imageHeight = 0;

                                        if (!isNullGroupImage)
                                        {
                                            imageWidth = checkImage.getWidth ();
                                            imageHeight = checkImage.getHeight ();
                                        }

                                        if (isNullGroupImage || (imageWidth >= minWidth && imageWidth <= maxWidth) && (imageHeight >= minHeight && imageHeight <= maxHeight))
                                        {
                                            boolean okUsernames = true;
                                            List <RequestCreateStickerGroup.LicensedUsers> users = null;
                                            List <RequestCreateStickerGroup.LicensedUsers> licensedUsers = null;

                                            String with_per = request.getWith_per ();
                                            if (Str.HasBool (with_per))
                                            {
                                                Boolean withPer = Str.ToBool (with_per);
                                                assert withPer != null;
                                                request.setWithPermission (withPer);
                                            }

                                            // sabt dastrasi , agar true bashad
                                            if (!isUpdate && request.isWithPermission ())
                                            {
                                                if (request.setLicensedUsers ())
                                                {
                                                    licensedUsers = request.getLicensedUsers ();

                                                    if (licensedUsers != null && licensedUsers.size () > 0)
                                                    {
                                                        String duplicateItems;
                                                        if ((duplicateItems = (checkForDuplicateItems (licensedUsers))) == null)
                                                        {
                                                            final FITD_Username fitd_username = new FITD_Username (usernamesService);
                                                            final ILUGroup iluGroup = new ILUGroup (groupsService , usernamesService);

                                                            users = new ArrayList <> ();

                                                            StickerAccessLevelType type;

                                                            UserProfileAccessLevel userProfileAccessLevel = new UserProfileAccessLevel (mainAccount);
                                                            for (var licensedUser : licensedUsers)
                                                            {
                                                                // type baray in ke in dastrasi baraye group hast ya user
                                                                type = StickerAccessLevelType.to (licensedUser.getType ());
                                                                if (type != null)
                                                                {
                                                                    if (type.equals (StickerAccessLevelType.user))
                                                                    {
                                                                        // agar user bod , username check mishavad, agar sahih bod , dakhl list users ezaf mishe baraye sabt
                                                                        fitd_username.check (licensedUser.getUsername ());
                                                                        if (!fitd_username.isOk ())
                                                                        {
                                                                            answer = fitd_username.getAnswer ();
                                                                            answer.setReqRes (req , res);
                                                                            l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.username_invalid.name ()) , ToJson.CreateClass.nj ("username" , licensedUser.getUsername ()) , csgType , true);
                                                                            okUsernames = false;
                                                                            break;
                                                                        }
                                                                        else
                                                                        {
                                                                            /*
                                                                             * barasi dastresi be user khaste shode
                                                                             */
                                                                            userProfileAccessLevel.setUser (fitd_username.getMainAccount ());
                                                                            if (userProfileAccessLevel.hasAccess (Which.find_me , Which.username))
                                                                            {
                                                                                licensedUser.setUsername (null);
                                                                                licensedUser.setMainAccount (fitd_username.getMainAccount ());
                                                                                users.add (licensedUser);
                                                                            }
                                                                            else
                                                                            {
                                                                                answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.user_not_found.name ());
                                                                                answer.put (AnswerToClient.CUK.which.name () , licensedUser.getUsername ());
                                                                                answer.setReqRes (req , res);
                                                                                l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.user_not_found.name ()) , ToJson.CreateClass.nj ("username" , licensedUser.getUsername ()) , csgType , true);
                                                                                okUsernames = false;
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        /*
                                                                         *  agar group bod ke check mikonam aval groupname va bad in ke karbar login shode modir group hast ya na
                                                                         */
                                                                        iluGroup.setGroupName (licensedUser.getUsername ());
                                                                        if (iluGroup.isValid ())
                                                                        {
                                                                            final Groups groups = iluGroup.getGroup ();
                                                                            assert groups != null;
                                                                            if (groups.getOwner ().getId () == mainAccount.getId ())
                                                                            {
                                                                                licensedUser.setUsername (null);
                                                                                licensedUser.setGroups (groups);
                                                                                users.add (licensedUser);
                                                                            }
                                                                            else
                                                                            {
                                                                                answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.you_do_not_own_a_group.name ());
                                                                                answer.put (AnswerToClient.CUK.which.name () , licensedUser.getUsername ());
                                                                                answer.setReqRes (req , res);
                                                                                l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_do_not_own_a_group.name ()) , ToJson.CreateClass.nj ("groupname" , licensedUser.getUsername ()) , csgType , true);
                                                                                okUsernames = false;
                                                                                break;
                                                                            }
                                                                        }
                                                                        else
                                                                        {
                                                                            answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.invalid_groupname.name ());
                                                                            answer.put (AnswerToClient.CUK.which.name () , licensedUser.getUsername ());
                                                                            answer.setReqRes (req , res);
                                                                            l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_groupname.name ()) , ToJson.CreateClass.nj ("groupname" , licensedUser.getUsername ()) , csgType , true);
                                                                            okUsernames = false;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.invalid_sticker_access_level_type.name ());
                                                                    l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_sticker_access_level_type.name ()) , ToJson.CreateClass.nj ("type" , licensedUser.getType ()) , csgType , true);
                                                                    break;
                                                                }

                                                            }
                                                        }
                                                        else
                                                        {
                                                            answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.duplicate_item_found.name ());
                                                            answer.put (KeyAnswer.duplicate_case.name () , duplicateItems);
                                                            answer.setReqRes (req , res);
                                                            l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.duplicate_item_found.name ()) , ToJson.CreateClass.nj ("duplicate_items" , duplicateItems) , csgType , true);
                                                            okUsernames = false;
                                                        }
                                                    }
                                                }
                                                else
                                                {
                                                    answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.error_licensed_users.name ());
                                                    answer.setReqRes (req , res);
                                                    l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.error_licensed_users.name ()) , null , csgType , true);
                                                }
                                            }

                                            if (okUsernames)
                                            {
                                                File saveTo = null;
                                                String codeStr = null;
                                                boolean ok = false;
                                                List <String> codes = new ArrayList <> ();
                                                String typeFile = null;

                                                if (!isNullGroupImage)
                                                {
                                                    int counter = 0;
                                                    Code code;

                                                    typeFile = FilenameUtils.getExtension (groupImage.getOriginalFilename ());

                                                    while ((++counter) < 10)
                                                    {
                                                        code = Code.CreateCodeLong ();
                                                        code.createCode ();
                                                        codeStr = code.getCode ();
                                                        codes.add (codeStr);
                                                        if (!(saveTo = new File (Path.StickTogether (Path.StickerGroups (mainAccount.getId ()) , codeStr + "." + typeFile))).exists ())
                                                        {
                                                            ok = true;
                                                            break;
                                                        }
                                                    }
                                                }

                                                if (isNullGroupImage || ok)
                                                {
                                                    try
                                                    {
                                                        if (isUpdate && !isNullGroupImage)
                                                            uploadedFilesService.Repository.delete (stickerGroups.getGroupImage ().getId () , mainAccount.getId ());
                                                        if (!isNullGroupImage)
                                                            Files.write (saveTo.toPath () , groupImage.getBytes ());

                                                        UploadedFiles images = null;
                                                        if (!isNullGroupImage)
                                                        {
                                                            images = new UploadedFiles ();
                                                            images.setFileFor (StickerGroups.class.getName ());
                                                            images.setName (codeStr);

                                                            images.setType (typeFile);
                                                            images.setWidth (imageWidth);
                                                            images.setHeight (imageHeight);
                                                            images.setSavedPath (saveTo.getParent ());
                                                            images.setSize (groupImage.getSize ());
                                                            images.setUploadedBy (mainAccount);

                                                            images = uploadedFilesService.Repository.save (images);
                                                        }

                                                        if (isNullGroupImage || images.getId () > 0)
                                                        {
                                                            if (!isUpdate)
                                                            {
                                                                stickerGroups = new StickerGroups ();
                                                                stickerGroups.setAddedBy (mainAccount);
                                                            }
                                                            else
                                                            {
                                                                DeletedOrEdited deletedOrEdited = new DeletedOrEdited ();
                                                                deletedOrEdited.setDeletedBy (mainAccount);
                                                                deletedOrEdited.setDeletedByClass (this.getClass ().getName ());
                                                                deletedOrEdited.setIdDeleted (stickerGroups.getId ());
                                                                deletedOrEdited.setType (DeletedOrEditedType.edited);
                                                                deletedOrEdited.setTableName (StickerGroups.TBNAME);
                                                                deletedOrEdited.setValue (ToJson.To (stickerGroups));
                                                                deletedOrEdited.setDescription ("edited info group sticker");

                                                                deletedOrEditedService.Repository.save (deletedOrEdited);
                                                            }

                                                            if (!isNullGroupImage)
                                                                stickerGroups.setGroupImage (images);

                                                            stickerGroups.setGroupName (request.getGroup_name ());
                                                            stickerGroups.setWithPermission (request.isWithPermission ());
                                                            stickerGroups.setDescription (request.getDescription ());

                                                            stickerGroups = stickerGroupsService.Repository.save (stickerGroups);

                                                            if (isUpdate || stickerGroups.getId () > 0)
                                                            {
                                                                if (!isUpdate && request.isWithPermission () && licensedUsers != null && licensedUsers.size () > 0)
                                                                {
                                                                    List <StickerAccessLevel> stickerAccessLevels = new ArrayList <> ();

                                                                    assert users != null;
                                                                    for (var user : users)
                                                                    {
                                                                        try
                                                                        {

                                                                            StickerAccessLevel stickerAccessLevel = new StickerAccessLevel ();

                                                                            if (user.getType ().equals (StickerAccessLevelType.user.name ()))
                                                                                stickerAccessLevel.setMainAccount (user.getMainAccount ());
                                                                            else
                                                                                stickerAccessLevel.setGroups (user.getGroups ());

                                                                            stickerAccessLevel.setStickerGroups (stickerGroups);
                                                                            stickerAccessLevels.add (stickerAccessLevel);
                                                                            stickerAccessLevel.setType (StickerAccessLevelType.to (user.getType ()));
                                                                        }
                                                                        catch (NullPointerException e)
                                                                        {
                                                                            l.n (Thread.currentThread ().getStackTrace () , e);
                                                                        }
                                                                    }
                                                                    hasStickerAccessLevel.getService ().Repository.saveAll (stickerAccessLevels);
                                                                }

                                                                String valAnswer;
                                                                if (isUpdate)
                                                                    valAnswer = ValAnswer.updated_sticker_group.name ();
                                                                else valAnswer = ValAnswer.added_sticker_group.name ();

                                                                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , valAnswer);
                                                                answer.put (AnswerToClient.CUK.id.name () , stickerGroups.getId ());
                                                                answer.setReqRes (req , res);
                                                                l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , valAnswer , csgType , false);
                                                            }
                                                            else
                                                                throw new IOException (ValAnswer.error_save_info_sticker.name ());
                                                        }
                                                        else
                                                            throw new IOException (ValAnswer.error_save_info_image.name ());
                                                    }
                                                    catch (IOException e)
                                                    {
                                                        answer = AnswerToClient.ServerError ();
                                                        answer.setReqRes (req , res);

                                                        String saveToPath;

                                                        saveToPath = saveTo.getPath ();

                                                        l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , e , ToJson.CreateClass.n ("error" , ValAnswer.error_write_file.name ()).put ("save_to" , saveToPath).toJson () , csgType , true);
                                                    }
                                                }
                                                else
                                                {
                                                    answer = AnswerToClient.ServerError ();
                                                    answer.setReqRes (req , res);
                                                    l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , ToJson.CreateClass.n ("error" , ValAnswer.error_create_name_group_image.name ()).put ("codes" , codes.toString ()).toJson () , csgType , true);
                                                }
                                            }
                                        }
                                        else
                                        {
                                            answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.invalid_width_or_height.name ());

                                            answer.put (AnswerToClient.CUK.acceptable_width_height.name () ,

                                                    ToJson.CreateClass.n (AnswerToClient.CUK.min_width.name () , minWidth)
                                                            .put (AnswerToClient.CUK.max_width.name () , maxWidth)
                                                            .put (AnswerToClient.CUK.min_width.name () , minWidth)
                                                            .put (AnswerToClient.CUK.min_height.name () , minHeight)
                                                            .put (AnswerToClient.CUK.max_height.name () , maxHeight).getCreateClass ())

                                                    .put (KeyAnswer.your_image_width_height.name () ,
                                                            ToJson.CreateClass.n (AnswerToClient.CUK.width.name () , imageWidth)
                                                                    .put (AnswerToClient.CUK.height.name () , imageHeight).getCreateClass ());

                                            answer.setReqRes (req , res);
                                            l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.invalid_width_or_height.name ()) , null , csgType , true);
                                        }
                                    }
                                    else
                                    {
                                        answer = AnswerToClient.ServerError ();
                                        answer.setReqRes (req , res);
                                        l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , ToJson.CreateClass.nj ("error" , DefaultKey.class.getName ()) , csgType , true);
                                    }
                                }
                                else
                                {
                                    if (maxSizeSticker == null)
                                    {
                                        answer = AnswerToClient.ServerError ();
                                        answer.setReqRes (req , res);
                                        l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , "@var maxSizeSticker == null" , csgType , true);
                                    }
                                    else
                                    {
                                        answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.the_size_of_the_image_is_large.name ());
                                        answer.put (AnswerToClient.CUK.acceptable_size.name () , GetSize.Get (maxSizeSticker));
                                        answer.put (KeyAnswer.your_image_size.name () , GetSize.Get (imageSize));
                                        answer.put (AnswerToClient.CUK.extra_size.name () , GetSize.Get ((imageSize - maxSizeSticker)));
                                        answer.setReqRes (req , res);
                                        l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.the_size_of_the_image_is_large.name ()) , null , csgType , true);
                                    }

                                }
                            }
                            else
                            {
                                answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.invalid_group_image.name ());
                                answer.setReqRes (req , res);
                                l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_group_image.name ()) , null , csgType , true);
                            }
                        }
                        else
                        {
                            answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.is_empty_group_image.name ());
                            answer.setReqRes (req , res);
                            l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.is_empty_group_image.name ()) , null , csgType , true);
                        }
                    }
                    else
                    {
                        answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.is_empty_group_name.name ());
                        answer.setReqRes (req , res);
                        l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.is_empty_group_name.name ()) , null , csgType , true);
                    }
                }

            }
            else
            {
                answer = AnswerToClient.RequestIsNull ();
                answer.setReqRes (req , res);
                l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null , csgType , true);
            }

        }
        else answer = both.getAnswerToClient ();

        return answer;
    }

    public String checkForDuplicateItems (List <RequestCreateStickerGroup.LicensedUsers> licensedUsers)
    {
        RequestCreateStickerGroup.LicensedUsers userI, userJ;
        for (int i = 0, len = licensedUsers.size (); i < len; i++)
        {
            userI = licensedUsers.get (i);
            for (int j = i + 1, lenJ = licensedUsers.size (); j < lenJ; j++)
            {
                userJ = licensedUsers.get (j);
                if (userI.getUsername ().equals (userJ.getUsername ()) && userI.getType ().equals (userJ.getType ()))
                    return licensedUsers.get (i).getUsername ();
            }
        }

        return null;
    }

    @RequestMapping (value = "/groups-ids")
    public AnswerToClient stickersGroups
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answer;

        CBSIL both = CBSIL.Both (null , req , res , codeLogin , userLoginService , giRouter , giType);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            List <Long> ids = stickerGroupsService.ids (mainAccount.getId ());
            if (ids != null && ids.size () > 0)
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());
                answer.put (AnswerToClient.CUK.ids.name () , ids);
                answer.setReqRes (req , res);
                l.n (null , giRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null , giType , false);
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found.name ());
                answer.setReqRes (req , res);
                l.n (null , giRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null , giType , true);
            }
        }
        else answer = both.getAnswerToClient ();

        return answer;
    }

    @RequestMapping (value = { "/group" , "/group/{id_group}" })
    public AnswerToClient stickersGroups
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @PathVariable (value = "id_group", required = false) String strIdGroup)
    {
        AnswerToClient answer = null;

        String request = ToJson.CreateClass.nj ("id_group" , strIdGroup);
        Object getAndDelete = getAndDelete (strIdGroup , res , req , codeLogin , gRouter , gType);

        if (getAndDelete instanceof AnswerToClient) answer = (AnswerToClient) getAndDelete;
        else
        {
            AnswerGetAndDelete answerGetAndDelete = (AnswerGetAndDelete) getAndDelete;
            MainAccount mainAccount = answerGetAndDelete.mainAccount;
            StickerGroups stickerGroups = answerGetAndDelete.stickerGroups;

            boolean accessLevel;

            if (stickerGroups.isWithPermission ())
            {
                if (stickerGroups.getAddedBy ().getId () == mainAccount.getId ()) accessLevel = true;
                else
                    accessLevel = (answer = hasStickerAccessLevel.hasAccess (stickerGroups , mainAccount , 0 , StickerAccessLevelType.user)) == null;
            }
            else accessLevel = true;

            if (accessLevel)
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());
                answer.put (KeyAnswer.name.name () , stickerGroups.getGroupName ());
                answer.put (KeyAnswer.des.name () , stickerGroups.getDescription ());
                answer.put (KeyAnswer.img_id.name () , stickerGroups.getGroupImage ().getId ());
                answer.put (KeyAnswer.added_at.name () , Time.toString (stickerGroups.getAddedAt ()));
                answer.setReqRes (req , res);
                l.n (request , gRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.found.name () , gType , false);
            }
            else
            {
                answer.setReqRes (req , res);
                l.n (request , gRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.access_denied.name ()) , null , gType , true);
            }
        }
        return answer;
    }

    @RequestMapping (value = { "/delete-sticker-group" , "/delete-sticker-group/{id_group}" })
    public AnswerToClient deleteStickerGroup
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @PathVariable (value = "id_group", required = false) String strIdGroup)
    {
        AnswerToClient answer;
        Object getAndDelete = getAndDelete (strIdGroup , res , req , codeLogin , dgRouter , dgType);
        if (getAndDelete instanceof AnswerToClient) answer = (AnswerToClient) getAndDelete;
        else
        {
            AnswerGetAndDelete answerGetAndDelete = (AnswerGetAndDelete) getAndDelete;
            MainAccount mainAccount = answerGetAndDelete.mainAccount;
            StickerGroups stickerGroups = answerGetAndDelete.stickerGroups;

            if (stickerGroups.getAddedBy ().getId () == mainAccount.getId ())
            {
                stickerGroupsService.Repository.delete (stickerGroups.getId () , mainAccount.getId ());
                uploadedFilesService.Repository.delete (stickerGroups.getGroupImage ().getId () , mainAccount.getId ());
                hasStickerAccessLevel.getService ().delete (stickerGroups.getId ());

                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.removed.name ());
                answer.setReqRes (req , res);
                l.n (null , dgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.removed.name () , dgType , false);
            }
            else
            {
                answer = AnswerToClient.AccessDenied ();
                answer.setReqRes (req , res);
                l.n (null , dgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.access_denied.name ()) , "stickerGroups.getAddedBy ().getId () != mainAccount.getId ()" , dgType , true);
            }
        }

        return answer;
    }

    /**
     * @return AnswerToClient OR AnswerGetAndDelete
     */
    private Object getAndDelete (String strIdGroup , HttpServletResponse res , HttpServletRequest req , String
            codeLogin , String r , SubmitRequestType t)
    {
        // r => Router , t => type

        AnswerToClient answer;

        String request = ToJson.CreateClass.nj ("id_group" , strIdGroup);
        CBSIL both = CBSIL.Both (null , req , res , codeLogin , userLoginService , r , t);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (!Str.IsEmpty (strIdGroup))
            {
                ID idGroup = new ID (strIdGroup);
                if (idGroup.isValid ())
                {
                    StickerGroups stickerGroups = stickerGroupsService.stickerGroups (idGroup.getId () , mainAccount.getId ());
                    if (stickerGroups != null) return new AnswerGetAndDelete (stickerGroups , mainAccount);
                    else
                    {
                        answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found.name ());
                        answer.setReqRes (req , res);
                        l.n (request , r , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null , t , true);
                    }
                }
                else
                {
                    answer = AnswerToClient.IdInvalid ();
                    answer.setReqRes (req , res);
                    l.n (request , r , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.id_invalid.name ()) , ToJson.CreateClass.nj ("id_group" , strIdGroup) , t , true);
                }
            }
            else
            {
                answer = AnswerToClient.RequestIsNull ();
                answer.setReqRes (req , res);
                l.n (null , r , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null , t , true);
            }
        }
        else answer = both.getAnswerToClient ();

        return answer;
    }

    private final static class AnswerGetAndDelete
    {
        private final StickerGroups stickerGroups;
        private final MainAccount mainAccount;

        public AnswerGetAndDelete (final StickerGroups _StickerGroups , final MainAccount _MainAccount)
        {
            this.stickerGroups = _StickerGroups;
            this.mainAccount = _MainAccount;
        }
    }

    private enum ValAnswer
    {
        is_empty_group_name, is_empty_group_image, invalid_group_image,
        error_create_name_group_image, error_write_file, error_save_info_image,
        error_save_info_sticker, added_sticker_group, updated_sticker_group,
        the_size_of_the_image_is_large, invalid_sticker_group_id, not_found_sticker_group_id,
        invalid_sticker_access_level_type, invalid_groupname,
        you_do_not_own_a_group, duplicate_item_found, error_licensed_users
    }

    private enum KeyAnswer
    {
        des, name, img_id, added_at, your_image_size,
        your_image_width_height,
        duplicate_case
    }

}
