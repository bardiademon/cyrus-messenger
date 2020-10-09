package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Stickers;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultKey;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups.StickerGroups;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups.StickerGroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickersService;
import com.bardiademon.CyrusMessenger.Model.Database.Images.UploadedImages;
import com.bardiademon.CyrusMessenger.Model.Database.Images.UploadedImagesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Default;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.GetSize;
import com.bardiademon.CyrusMessenger.bardiademon.IO.CheckImage;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping (value = Domain.RNGap.STICKERS, method = RequestMethod.POST)
public final class RestStickers
{

    private final StickersService stickersService;
    private final StickerGroupsService stickerGroupsService;
    private final UserLoginService userLoginService;
    private final UploadedImagesService uploadedImagesService;
    private final DefaultService defaultService;

    /**
     * csg => Create Sticker Group
     */
    private final String csgRouter;
    private final SubmitRequestType csgType;

    @Autowired
    public RestStickers
            (StickersService _StickersService ,
             StickerGroupsService _StickerGroupsService ,
             UserLoginService _UserLoginService ,
             UploadedImagesService _UploadedImagesService , DefaultService _DefaultService)
    {
        this.stickersService = _StickersService;
        this.stickerGroupsService = _StickerGroupsService;
        this.userLoginService = _UserLoginService;
        this.uploadedImagesService = _UploadedImagesService;
        this.defaultService = _DefaultService;
        this.csgRouter = Domain.RNGap.STICKERS + "/create-sticker-group";
        this.csgType = SubmitRequestType.create_sticker_group;
    }

    @RequestMapping (value = "/create-sticker-group")
    public AnswerToClient createStickerGroup
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @ModelAttribute RequestCreateStickerGroup request)
    {
        AnswerToClient answer;

        String reqStr = ToJson.To (request);

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , csgRouter , SubmitRequestType.create_sticker_group);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                if (!Str.IsEmpty (request.getGroup_name ()))
                {
                    MultipartFile groupImage = request.getGroup_image ();
                    if (groupImage != null)
                    {
                        CheckImage checkImage = new CheckImage ();
                        if (checkImage.valid (groupImage))
                        {
                            long imageSize = groupImage.getSize ();

                            Long maxSizeSticker = defaultService.getLong (DefaultKey.max_size_sticker);
                            if (maxSizeSticker != null && imageSize <= maxSizeSticker)
                            {
                                Integer minWidth, maxWidth, minHeight, maxHeight;

                                if ((minWidth = defaultService.getInt (DefaultKey.min_w_sticker_image)) != null
                                        && (maxWidth = defaultService.getInt (DefaultKey.max_w_sticker_image)) != null
                                        && (minHeight = defaultService.getInt (DefaultKey.min_h_sticker_image)) != null
                                        && (maxHeight = defaultService.getInt (DefaultKey.max_h_sticker_image)) != null)
                                {
                                    final int imageWidth = checkImage.getWidth ();
                                    final int imageHeight = checkImage.getHeight ();
                                    if ((imageWidth >= minWidth && imageWidth <= maxWidth) && (imageHeight >= minHeight && imageHeight <= maxHeight))
                                    {
                                        int counter = 0;
                                        boolean ok = false;
                                        List <String> codes = new ArrayList <> ();
                                        String codeStr = null;
                                        File saveTo = null;
                                        Code code;

                                        final String typeFile = FilenameUtils.getExtension (groupImage.getOriginalFilename ());

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
                                        if (ok)
                                        {
                                            try
                                            {
                                                Files.write (saveTo.toPath () , groupImage.getBytes ());

                                                UploadedImages uploadedImages = new UploadedImages ();
                                                uploadedImages.setImageFor (StickerGroups.class.getName ());
                                                uploadedImages.setName (codeStr);
                                                uploadedImages.setType (typeFile);
                                                uploadedImages.setWidth (imageWidth);
                                                uploadedImages.setHeight (imageHeight);
                                                uploadedImages.setSavedPath (saveTo.getParent ());
                                                uploadedImages.setSize (groupImage.getSize ());
                                                uploadedImages.setUploadedBy (mainAccount);

                                                uploadedImages = uploadedImagesService.Repository.save (uploadedImages);

                                                if (uploadedImages.getId () > 0)
                                                {
                                                    StickerGroups stickerGroups = new StickerGroups ();
                                                    stickerGroups.setAddedBy (mainAccount);
                                                    stickerGroups.setGroupImage (uploadedImages);
                                                    stickerGroups.setGroupName (request.getGroup_name ());
                                                    stickerGroups.setDescription (request.getDescription ());

                                                    stickerGroups = stickerGroupsService.Repository.save (stickerGroups);

                                                    if (stickerGroups.getId () > 0)
                                                    {
                                                        answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.added_sticker_group.name ());
                                                        answer.put (AnswerToClient.CUK.id.name () , stickerGroups.getId ());
                                                        answer.setReqRes (req , res);
                                                        l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , ValAnswer.added_sticker_group.name () , csgType , false);
                                                    }
                                                    else
                                                        throw new IOException (ValAnswer.error_save_info_sticker.name ());
                                                }
                                                else throw new IOException (ValAnswer.error_save_info_image.name ());
                                            }
                                            catch (IOException e)
                                            {
                                                answer = AnswerToClient.ServerError ();
                                                answer.setReqRes (req , res);
                                                l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , e , ToJson.CreateClass.n ("error" , ValAnswer.error_write_file.name ()).put ("save_to" , saveTo.getPath ()).toJson () , csgType , true);
                                            }
                                        }
                                        else
                                        {
                                            answer = AnswerToClient.ServerError ();
                                            answer.setReqRes (req , res);
                                            l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , ToJson.CreateClass.n ("error" , ValAnswer.error_create_name_group_image.name ()).put ("codes" , codes.toString ()).toJson () , csgType , true);
                                        }
                                    }
                                    else
                                    {
                                        answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.invalid_width_or_height.name ());

                                        answer.put (KeyAnswer.acceptable_width_height.name () ,
                                                ToJson.CreateClass.n (KeyAnswer.min_width.name () , minWidth)
                                                        .put (KeyAnswer.max_width.name () , maxWidth)
                                                        .put (KeyAnswer.min_width.name () , minWidth)
                                                        .put (KeyAnswer.min_height.name () , minHeight)
                                                        .put (KeyAnswer.max_height.name () , maxHeight).getCreateClass ());

                                        answer.put (KeyAnswer.your_image_width_height.name () ,
                                                ToJson.CreateClass.n (KeyAnswer.width.name () , imageWidth)
                                                        .put (KeyAnswer.height.name () , imageHeight).getCreateClass ());

                                        answer.setReqRes (req , res);
                                        l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_width_or_height.name ()) , null , csgType , true);
                                    }
                                }
                                else
                                {
                                    answer = AnswerToClient.ServerError ();
                                    answer.setReqRes (req , res);
                                    l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , ToJson.CreateClass.nj ("error" , Default.class.getName ()) , csgType , true);
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
                                    answer.put (KeyAnswer.acceptable_size.name () , GetSize.Get (maxSizeSticker));
                                    answer.put (KeyAnswer.your_image_size.name () , GetSize.Get (imageSize));
                                    answer.put (KeyAnswer.extra_size.name () , GetSize.Get ((imageSize - maxSizeSticker)));
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
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.request_is_null.name ());
                answer.setReqRes (req , res);
                l.n (reqStr , csgRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null , csgType , true);
            }

        }
        else answer = both.getAnswerToClient ();

        return answer;
    }

    private enum ValAnswer
    {
        is_empty_group_name, is_empty_group_image, invalid_group_image,
        error_create_name_group_image, error_write_file, error_save_info_image,
        error_save_info_sticker, added_sticker_group,
        the_size_of_the_image_is_large,
        invalid_width_or_height
    }

    private enum KeyAnswer
    {
        acceptable_size, your_image_size, extra_size,
        your_image_width_height,
        min_width, min_height,
        max_width, max_height,
        width, height,
        acceptable_width_height
    }

}