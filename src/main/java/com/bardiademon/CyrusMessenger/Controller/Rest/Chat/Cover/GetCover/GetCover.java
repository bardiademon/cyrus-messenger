package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Cover.GetCover;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


@RestController
@RequestMapping (value = "/api/chat/cover", method = RequestMethod.GET)
public class GetCover
{
    private MainAccountService mainAccountService;

    @Autowired
    public GetCover (MainAccountService _MainAccountService)
    {
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping (value = {"/{username}" , "/" , ""}, produces = MediaType.IMAGE_JPEG_VALUE, method = RequestMethod.GET)
    public @ResponseBody
    byte[] get (@PathVariable (value = "username", required = false) String username , HttpServletResponse response)
    {
        response.setContentType ("image/jpeg");

        if (username == null || username.equals ("")) return error ();

        MainAccount mainAccount = findUsername (username);
        if (mainAccount != null)
        {
            String cover = mainAccount.getCover ();
            File fileCover = Path.StickTogetherFile (Path.COVER_USER , cover);
            if (fileCover.exists ()) return toByte (fileCover);
            else return error ();
        }
        else return error ();
    }

    private MainAccount findUsername (String username)
    {
        return mainAccountService.Repository.findByUsername (username);
    }

    private byte[] error ()
    {
        return toByte (new File (Path.IMAGE_NOT_FOUND));
    }

    private byte[] toByte (File file)
    {
        try
        {
            return IOUtils.toByteArray (new FileInputStream (file));
        }
        catch (IOException ignored)
        {
        }
        return null;
    }

}
