package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Cover.UploadCover;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.VCodeLogin;
import com.bardiademon.CyrusMessenger.bardiademon.Default.DSize;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.GetSize;
import com.bardiademon.CyrusMessenger.bardiademon.IO.CheckImage;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@RestController
@RequestMapping (value = "/api/chat/upload_cover", method = RequestMethod.POST)
public class UploadCover
{

    private UserLoginService userLoginService;
    private MainAccountService mainAccountService;

    public UploadCover (UserLoginService _UserLoginService , MainAccountService _MainAccountService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping ({"/" , ""})
    public AnswerToClient upload (HttpServletResponse res , @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
                                  @ModelAttribute RequestUploadCover requestUploadCover)
    {
        AnswerToClient answerToClient;

        VCodeLogin vCodeLogin = new VCodeLogin ();
        if (vCodeLogin.IsValid (userLoginService.Repository , codeLogin))
        {
            MultipartFile cover = requestUploadCover.getCover ();
            if (cover != null)
            {

                CheckImage checkImage = new CheckImage ();
                if (checkImage.check (cover , FilenameUtils.getExtension (cover.getOriginalFilename ())))
                {
                    if (cover.getSize () > DSize.SIZE_COVER)
                    {
                        answerToClient = AnswerToClient.error400 ();
                        answerToClient.put (AnswerKey.answer.name () , AnswerValue.size_is_large.name ());
                        answerToClient.put (AnswerKey.max_size.name () , GetSize.Get (DSize.SIZE_COVER));
                        answerToClient.put (AnswerKey.max_size_byte.name () , DSize.SIZE_COVER);
                    }
                    else
                    {
                        MainAccount mainAccount = vCodeLogin.getMainAccount ();
                        if (mainAccount.hasCover () && !requestUploadCover.isReplace ())
                        {
                            answerToClient = AnswerToClient.error400 ();
                            answerToClient.put (AnswerKey.answer.name () , AnswerValue.image_is_exists.name ());
                        }
                        else
                        {
                            String nameOldCover = null;
                            boolean hasOldCover;
                            if ((hasOldCover = (mainAccount.hasCover ()))) nameOldCover = mainAccount.getCover ();


                            final int MAX_CREATE = 5;
                            int counter = 0;
                            boolean createName = false;
                            File file = null;
                            String name = "";
                            while ((counter++) < MAX_CREATE)
                            {
                                name = String.format ("%s.%s" , Code.Name () , checkImage.getExtension ());
                                file = new File (Path.StickTogether (Path.COVER_USER , name));
                                if (!file.exists ())
                                {
                                    createName = true;
                                    break;
                                }
                            }
                            if (createName)
                            {
                                try
                                {
                                    Files.write (file.toPath () , cover.getBytes ());
                                    if (file.exists ())
                                    {
                                        mainAccount.setCover (name);
                                        MainAccount save = mainAccountService.Repository.save (mainAccount);
                                        if (save != null)
                                        {
                                            if (hasOldCover)
                                                (new File (Path.StickTogether (file.getParent () , nameOldCover))).delete ();

                                            answerToClient = AnswerToClient.OK ();
                                            answerToClient.put (AnswerKey.answer.name () , AnswerValue.image_uploaded.name ());
                                        }
                                        else
                                        {
                                            file.delete ();
                                            answerToClient = AnswerToClient.ServerError ();
                                        }
                                    }
                                    else throw new IOException ("Error write");
                                }
                                catch (IOException e)
                                {
                                    answerToClient = AnswerToClient.ServerError ();
                                }
                            }
                            else answerToClient = AnswerToClient.ServerError ();
                        }
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.error400 ();
                    answerToClient.put (AnswerKey.answer.name () , AnswerValue.is_not_image.name ());
                }
            }
            else
            {
                answerToClient = AnswerToClient.error400 ();
                answerToClient.put (AnswerKey.answer.name () , AnswerValue.file_not_found.name ());
            }
        }
        else answerToClient = AnswerToClient.NotLoggedIn ();

        answerToClient.setResponse (res);

        return answerToClient;
    }


    private enum AnswerKey
    {
        answer, max_size, max_size_byte

    }

    private enum AnswerValue
    {
        size_is_large, image_is_exists, is_not_image, image_uploaded, file_not_found
    }
}
