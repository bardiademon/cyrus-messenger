package com.bardiademon.CyrusMessenger.Controller.Rest.RestConfirmPhone;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VPhone;
import com.bardiademon.CyrusMessenger.Model.AfterConfirm;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCode;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.SMS.SendSMS.ConfirmPhone;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Default;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping (value = "/api/confirm_phone", method = RequestMethod.POST)
public class RestConfirmPhone
{

    private final MainAccountService Service;

    private final ConfirmCodeService confirmCodeService;

    public RestConfirmPhone (MainAccountService Service , ConfirmCodeService _ConfirmCodeService)
    {
        this.Service = Service;
        this.confirmCodeService = _ConfirmCodeService;
    }

    @RequestMapping ({"/" , ""})
    public AnswerToClient confirmPhone (@RequestParam String phone , @RequestParam String region)
    {
        AnswerToClient answerToClient;
        if (phone == null)
        {
            answerToClient = new AnswerToClient (400 , false);
            answerToClient.put ("answer" , "Phone is null");
        }
        else
        {
            VPhone vPhone = new VPhone (phone , region);
            if (vPhone.check ())
            {
                answerToClient = new AnswerToClient (400 , false);
                answerToClient.put ("answer" , "Phone is invalid");
            }
            else
            {
                MainAccount byPhone = Service.Repository.findByPhone (vPhone.getPhone ());
                if (byPhone != null)
                {
                    ConfirmPhone confirmPhone = new ConfirmPhone (byPhone.getName () , byPhone.getPhone ());

                    ConfirmCode confirmCode = new ConfirmCode ();
                    confirmCode.setCode (String.valueOf (ConfirmPhone.getCode ()));
                    confirmCode.setMainAccount (byPhone);
                    confirmCode.setConfirmCodeFor (ConfirmCodeFor.phone);
                    confirmCode.setSendCodeTo (vPhone.getPhone ());

                    confirmCode.setTimeToBeOutdated (LocalDateTime.now ().plusMinutes (Default.PVCD));

                    ConfirmCode save = confirmCodeService.Repository.save (confirmCode);
                    if (save != null && confirmPhone.isSend ())
                    {
                        answerToClient = new AnswerToClient (200 , false);
                        answerToClient.put ("answer" , "Code send");
                        answerToClient.put ("id" , save.getId ());
                    }
                    else
                    {
                        answerToClient = new AnswerToClient (500 , false);
                        answerToClient.put ("answer" , "Error");
                    }
                }
                else
                {
                    answerToClient = new AnswerToClient (400 , false);
                    answerToClient.put ("answer" , "Phone not exists");
                }
            }
        }
        return answerToClient;
    }

    @RequestMapping (value = "code", method = RequestMethod.POST)
    public AnswerToClient getCode (@RequestParam String id , @RequestParam String code , @RequestParam String phone)
    {
        AnswerToClient answerToClient = new AnswerToClient (400 , false);
        if (code.matches ("[0-9]*") && id.matches ("[0-9]*"))
        {
            ConfirmCode findCode = confirmCodeService.Repository.findCode (Long.parseLong (id) , code , phone , LocalDateTime.now ());
            if (findCode != null)
            {
                new AfterConfirm (Service , confirmCodeService , findCode).confirm (ConfirmCodeFor.phone);

                answerToClient = new AnswerToClient (200 , true);
                answerToClient.put ("answer" , "Phone confirmed");
            }
        }
        return answerToClient;
    }

}
