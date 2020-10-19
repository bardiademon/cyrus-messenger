package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Stickers.Stickers;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.HasStickerAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultKey;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel.StickerAccessLevelService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel.StickerAccessLevelType;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups.StickerGroups;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups.StickerGroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.Stickers;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickersService;
import com.bardiademon.CyrusMessenger.Model.Database.Images.Images;
import com.bardiademon.CyrusMessenger.Model.Database.Images.ImagesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.IO.CheckImage;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
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
@RequestMapping (value = Domain.RNGap.STICKERS, method = RequestMethod.POST)
public final class RestStickers
{
    private final StickersService stickersService;
    private final StickerGroupsService stickerGroupsService;
    private final UserLoginService userLoginService;
    private final DefaultService defaultService;
    private final ImagesService imagesService;

    /**
     * as => AddStickers
     */
    private final String asRouter;
    private final SubmitRequestType asType;

    /**
     * goi => GetOneInfo
     */
    private final String goiRouter;
    private final SubmitRequestType goiType;

    /**
     * d => delete
     */
    private final String dRouter;
    private final SubmitRequestType dType;

    private final HasStickerAccessLevel hasStickerAccessLevel;

    @Autowired
    public RestStickers (
            final StickersService _StickersService ,
            final StickerGroupsService _StickerGroupsService ,
            final UserLoginService _UserLoginService ,
            final DefaultService _DefaultService ,
            final ImagesService _ImagesService ,
            final StickerAccessLevelService _StickerAccessLevelService)
    {
        this.stickersService = _StickersService;
        this.stickerGroupsService = _StickerGroupsService;
        this.userLoginService = _UserLoginService;
        this.defaultService = _DefaultService;
        this.imagesService = _ImagesService;

        this.asRouter = Domain.RNGap.STICKERS + "/add-stickers";
        this.asType = SubmitRequestType.add_stickers;

        this.goiRouter = Domain.RNGap.STICKERS + "/get-one-info";
        this.goiType = SubmitRequestType.get_one_info_sticker;

        this.dRouter = Domain.RNGap.STICKERS + "/delete";
        this.dType = SubmitRequestType.delete_sticker;

        this.hasStickerAccessLevel = new HasStickerAccessLevel (_StickerAccessLevelService);
    }

    @RequestMapping (value = "/add-sticker")
    public AnswerToClient addStickers
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @ModelAttribute RequestAddStickers request)
    {

        final String strRequest = ToJson.To (request);

        AnswerToClient answer;
        final CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , asRouter , asType);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            final MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                final ID idGroup = new ID (request.getId_group ());
                if (idGroup.isValid ())
                {
                    StickerGroups stickerGroups = stickerGroupsService.stickerGroups (idGroup.getId () , mainAccount.getId ());
                    if (stickerGroups != null)
                    {
                        final MultipartFile multipartFile = request.getImage ();
                        if (multipartFile != null && !multipartFile.isEmpty ())
                        {
                            CheckImage checkImage = new CheckImage ();
                            if (checkImage.valid (multipartFile))
                            {
                                final Integer maxLenName = defaultService.getInt (DefaultKey.sticker_max_len_name);
                                if (maxLenName != null)
                                {
                                    if (request.getName ().length () <= maxLenName)
                                    {
                                        if ((answer = checkImage.CSWH (req , res , strRequest , asRouter , mainAccount , asType , DefaultKey.max_size_sticker , DefaultKey.min_w_sticker_image , DefaultKey.max_w_sticker_image , DefaultKey.min_h_sticker_image , DefaultKey.max_h_sticker_image , defaultService)) == null)
                                        {
                                            boolean ok = false;
                                            String name = null;
                                            File file = null;
                                            List <String> codes = new ArrayList <> ();

                                            final String type = FilenameUtils.getExtension (multipartFile.getOriginalFilename ());

                                            Code code;

                                            int counter = 0;

                                            while ((++counter <= 10))
                                            {
                                                code = Code.CreateCodeLong ();
                                                code.createCode ();
                                                name = code.getCode ();
                                                if (!(file = (new File (Path.StickTogether (type , Str.toArray (Path.Stickers (mainAccount.getId ()) , name))))).exists ())
                                                {
                                                    ok = true;
                                                    break;
                                                }
                                                else codes.add (name);
                                            }
                                            if (ok && !file.exists ())
                                            {
                                                try
                                                {
                                                    Files.write (file.toPath () , multipartFile.getBytes ());

                                                    if (file.exists ())
                                                    {
                                                        Images image = new Images ();
                                                        image.setUploadedBy (mainAccount);
                                                        image.setSavedPath (file.getParent ());
                                                        image.setImageFor (Stickers.class.getName ());
                                                        image.setWidth (checkImage.getWidth ());
                                                        image.setHeight (checkImage.getHeight ());
                                                        image.setSize (checkImage.getSize ());
                                                        image.setName (name);
                                                        image.setType (type);

                                                        image = imagesService.Repository.save (image);
                                                        if (image.getId () > 0)
                                                        {
                                                            Stickers sticker = new Stickers ();
                                                            sticker.setGroup (stickerGroups);
                                                            sticker.setName (request.getName ());
                                                            sticker.setStickerImage (image);

                                                            sticker = stickersService.Repository.save (sticker);
                                                            if (sticker.getId () > 0)
                                                            {
                                                                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.added.name ());
                                                                answer.put (AnswerToClient.CUK.id.name () , sticker.getId ());
                                                                answer.setReqRes (req , res);
                                                                l.n (strRequest , asRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.added.name () , asType , false);
                                                            }
                                                            else throw new IOException ("Save sticker info error");
                                                        }
                                                        else throw new IOException ("Save image info error");
                                                    }
                                                    else throw new IOException ("Write image error");
                                                }
                                                catch (IOException e)
                                                {
                                                    answer = AnswerToClient.ServerError ();
                                                    answer.setReqRes (req , res);
                                                    l.n (strRequest , asRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , ToJson.CreateClass.nj ("file" , file.getPath ()) , asType , true);
                                                }
                                            }
                                            else
                                            {
                                                answer = AnswerToClient.ServerError ();
                                                answer.setReqRes (req , res);
                                                l.n (strRequest , asRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , ToJson.CreateClass.nj ("codes" , codes.toString ()) , asType , true);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.the_name_is_too_long.name ());
                                        answer.setReqRes (req , res);
                                        l.n (strRequest , asRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.the_name_is_too_long.name ()) , ToJson.CreateClass.nj ("name" , request.getName ()) , asType , true);
                                    }
                                }
                                else
                                {
                                    answer = AnswerToClient.ServerError ();
                                    answer.setReqRes (req , res);
                                    l.n (strRequest , asRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , null , asType , true);
                                }
                            }
                            else
                            {
                                answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.invalid_image.name ());
                                answer.setReqRes (req , res);
                                l.n (strRequest , asRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_image.name ()) , null , asType , true);
                            }
                        }
                        else
                        {
                            answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.is_empty_image.name ());
                            answer.setReqRes (req , res);
                            l.n (strRequest , asRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.is_empty_image.name ()) , null , asType , true);
                        }
                    }
                    else
                    {
                        answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.not_found_id.name ());
                        answer.setReqRes (req , res);
                        l.n (strRequest , asRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found_id.name ()) , ToJson.CreateClass.nj ("id_group" , idGroup.getId ()) , asType , true);
                    }
                }
                else
                {
                    answer = AnswerToClient.IdInvalid ();
                    answer.setReqRes (req , res);
                    l.n (strRequest , asRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.id_invalid.name ()) , ToJson.CreateClass.nj ("id_group" , idGroup.getIdObj ()) , asType , true);
                }
            }
            else
            {
                answer = AnswerToClient.RequestIsNull ();
                answer.setReqRes (req , res);
                l.n (null , asRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null , asType , true);
            }
        }
        else answer = both.getAnswerToClient ();

        return answer;
    }

    @RequestMapping (value = { "/get-one-info" , "/get-one-info/{id_sticker}" })
    public AnswerToClient getOneInfo
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @PathVariable (value = "id_sticker", required = false) String strIdSticker)
    {
        AnswerToClient answer;
        // A => Answer
        ACheckRequestGD aCheckRequestGD = checkRequestGD (res , req , strIdSticker , codeLogin , dRouter , dType);
        if (aCheckRequestGD.isOk ())
        {
            final Stickers sticker = aCheckRequestGD.getStickers ();
            final MainAccount mainAccount = aCheckRequestGD.getMainAccount ();
            final String request = aCheckRequestGD.getRequest ();

            StickerGroups stickerGroup = sticker.getGroup ();
            if (!stickerGroup.isWithPermission () || hasStickerAccessLevel.hasAccess (stickerGroup , mainAccount.getId () , StickerAccessLevelType.user))
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());
                answer.put (KeyAnswer.info.name () , ToJson.CreateClass.n (KeyAnswer.name.name () , sticker.getName ())
                        .put (KeyAnswer.image_id.name () , sticker.getStickerImage ().getId ())
                        .put (KeyAnswer.group_id.name () , stickerGroup.getId ()).getCreateClass ());

                answer.setReqRes (req , res);
                l.n (request , goiRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.found.name () , goiType , false);
            }
            else
            {
                answer = AnswerToClient.AccessDenied ();
                answer.setReqRes (req , res);
                l.n (request , goiRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.access_denied.name ()) , null , goiType , true);
            }
        }
        else answer = aCheckRequestGD.getAnswer ();

        return answer;
    }

    @RequestMapping (value = { "/delete" , "/delete/{id_sticker}" })
    public AnswerToClient delete
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @PathVariable (value = "id_sticker", required = false) String strIdSticker)
    {
        AnswerToClient answer;

        // A => Answer
        ACheckRequestGD aCheckRequestGD = checkRequestGD (res , req , strIdSticker , codeLogin , dRouter , dType);
        if (aCheckRequestGD.isOk ())
        {
            final Stickers sticker = aCheckRequestGD.getStickers ();
            final MainAccount mainAccount = aCheckRequestGD.getMainAccount ();
            final String request = aCheckRequestGD.getRequest ();

            if (sticker.getGroup ().getAddedBy ().getId () == mainAccount.getId ())
            {
                sticker.setDeleted (true);
                sticker.setDeletedAt (LocalDateTime.now ());

                stickersService.Repository.save (sticker);

                Images stickerImage = sticker.getStickerImage ();

                stickerImage.setDeleted (true);
                stickerImage.setDeletedAt (LocalDateTime.now ());
                imagesService.Repository.save (stickerImage);

                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.removed.name ());
                answer.setReqRes (req , res);
                l.n (request , dRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.removed.name () , dType , true);
            }
            else
            {
                answer = AnswerToClient.AccessDenied ();
                answer.setReqRes (req , res);
                l.n (request , dRouter , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.access_denied.name ()) , null , dType , true);
            }
        }
        else answer = aCheckRequestGD.getAnswer ();

        return answer;
    }

    /**
     * GD => Get Delete
     */
    private ACheckRequestGD checkRequestGD (HttpServletResponse res , HttpServletRequest req , String
            strIdSticker , String codeLogin , String router , SubmitRequestType type)
    {
        final ACheckRequestGD aCheckRequestGD = new ACheckRequestGD ();

        AnswerToClient answer;

        String request = ToJson.CreateClass.nj ("id_sticker" , strIdSticker);

        aCheckRequestGD.setRequest (request);

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            aCheckRequestGD.setMainAccount (mainAccount);


            ID idSticker = new ID (strIdSticker);
            if (idSticker.isValid ())
            {
                Stickers sticker = stickersService.getSticker (idSticker.getId ());
                if (sticker != null)
                {
                    aCheckRequestGD.setOk (true);
                    aCheckRequestGD.setStickers (sticker);
                    aCheckRequestGD.setAnswer (null);
                    return aCheckRequestGD;
                }
                else
                {
                    answer = AnswerToClient.IdInvalid (ValAnswer.not_found_sticker_id.name ());
                    answer.setReqRes (req , res);
                    l.n (request , router , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_found_sticker_id.name ()) , request , type , true);
                }
            }
            else
            {
                answer = AnswerToClient.IdInvalid ();
                answer.setReqRes (req , res);
                l.n (request , router , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.id_invalid.name ()) , request , type , true);
            }
        }
        else answer = both.getAnswerToClient ();

        aCheckRequestGD.setAnswer (answer);

        return aCheckRequestGD;
    }

    private static class ACheckRequestGD
    {
        private boolean ok;
        private AnswerToClient answer;
        private MainAccount mainAccount;
        private Stickers stickers;
        private String request;

        public void setOk (boolean ok)
        {
            this.ok = ok;
        }

        public boolean isOk ()
        {
            return ok;
        }

        public String getRequest ()
        {
            return request;
        }

        public void setRequest (String request)
        {
            this.request = request;
        }

        public AnswerToClient getAnswer ()
        {
            return answer;
        }

        public void setAnswer (AnswerToClient answer)
        {
            this.answer = answer;
        }

        public MainAccount getMainAccount ()
        {
            return mainAccount;
        }

        public void setMainAccount (MainAccount mainAccount)
        {
            this.mainAccount = mainAccount;
        }

        public Stickers getStickers ()
        {
            return stickers;
        }

        public void setStickers (Stickers stickers)
        {
            this.stickers = stickers;
        }
    }

    private enum KeyAnswer
    {
        info, name, image_id, group_id
    }

    private enum ValAnswer
    {
        is_empty_image, invalid_image, the_name_is_too_long, not_found_sticker_id
    }
}
