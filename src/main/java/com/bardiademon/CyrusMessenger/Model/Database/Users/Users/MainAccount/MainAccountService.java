package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Modify.ModifyInfoUser.RequestMIU;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestRegister.RegisterRequest;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernameFor;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.Usernames;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChat;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChatRepository;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileRepository;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor.ShowChatFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor.ShowChatForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCode;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed.Confirmed;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed.ConfirmedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails.EmailFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails.UserEmails;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails.UserEmailsService;
import com.bardiademon.CyrusMessenger.bardiademon.Hash256;
import static com.bardiademon.CyrusMessenger.bardiademon.Str.IsEmpty;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MainAccountService
{

    public final MainAccountRepository Repository;
    public final SecurityUserChatRepository repositorySecurityChat;
    public final SecurityUserProfileRepository repositorySecurityProfile;
    public final ShowChatForService showChatForService;
    private final ConfirmedService confirmedService;
    public final UsernamesService usernamesService;
    private final UserEmailsService userEmailsService;

    @Autowired
    public MainAccountService
            (MainAccountRepository Repository ,
             SecurityUserChatRepository RepositorySecurityChat ,
             SecurityUserProfileRepository RepositorySecurityProfile ,
             ShowChatForService _ShowChatForService ,
             ConfirmedService _ConfirmedService ,
             UsernamesService _UsernamesService ,
             UserEmailsService _UserEmailsService
            )
    {
        this.Repository = Repository;
        this.repositorySecurityChat = RepositorySecurityChat;
        this.repositorySecurityProfile = RepositorySecurityProfile;
        this.showChatForService = _ShowChatForService;
        this.confirmedService = _ConfirmedService;
        this.usernamesService = _UsernamesService;
        this.userEmailsService = _UserEmailsService;
    }


    public boolean newAccount (RegisterRequest registerRequest , Confirmed confirmedPhone , Confirmed confirmedEmail , ConfirmCodeService confirmCodeService)
    {
        MainAccount mainAccount = new MainAccount ();
        mainAccount.setName (registerRequest.getName ());
        mainAccount.setFamily (registerRequest.getFamily ());
        mainAccount.setPhone (confirmedPhone.getValue ());

        Usernames usernames = new Usernames ();
        usernames.setUsername (registerRequest.getUsername ());
        usernames.setUsernameFor (UsernameFor.user);
        usernames = usernamesService.Repository.save (usernames);

        mainAccount.setUsername (usernames);
        mainAccount.setPassword ((new Hash256 ()).hash (registerRequest.getPassword ()));

        MainAccount save = Repository.save (mainAccount);

        if (confirmedEmail != null)
        {
            UserEmails userEmails = new UserEmails ();
            userEmails.setConfirmed (true);
            userEmails.setConfirmedAt (confirmedEmail.getConfirmCode ().getTimeToConfirmed ());
            userEmails.setEmail (confirmedEmail.getValue ());
            userEmails.setEmailFor (EmailFor.ma);
            userEmails.setMainAccount (save);

            userEmails = userEmailsService.Repository.save (userEmails);
            save.setEmail (userEmails);

            Repository.save (save);
        }

        usernames.setMainAccount (save);
        usernamesService.Repository.save (usernames);

        SecurityUserProfile securityUserProfile = new SecurityUserProfile ();
        securityUserProfile.setMainAccount (save);

        SecurityUserChat securityUserChat = new SecurityUserChat ();
        securityUserChat.setCanSendNumberOfMessageUnread (0);
        securityUserChat.setMainAccount (save);

        repositorySecurityProfile.save (securityUserProfile);
        SecurityUserChat newSecurityUserChat = repositorySecurityChat.save (securityUserChat);

        ShowChatFor showChatFor = new ShowChatFor ();
        showChatFor.setSecurityUserChat (newSecurityUserChat);


        ConfirmCode confirmCode = confirmedPhone.getConfirmCode ();
        confirmCode.setMainAccount (mainAccount);
        confirmCodeService.Repository.save (confirmCode);


        return mainAccount.getId () > 0;

    }

    public RequestMIU updateInfoUser (MainAccount mainAccount , RequestMIU req)
    {
        if (!IsEmpty (req.getBio ()))
        {
            mainAccount.setBio (req.getBio ());
            req.setUpdatedBio ();
        }

        if (!IsEmpty (req.getName ()))
        {
            mainAccount.setName (req.getName ());
            req.setUpdatedName ();
        }

        if (!IsEmpty (req.getFamily ()))
        {
            mainAccount.setFamily (req.getFamily ());
            req.setUpdatedFamily ();
        }

        if (!IsEmpty (req.getGender ()))
        {
            mainAccount.setGender (UserGender.to (req.getGender ()));
            req.setUpdatedGender ();
        }

        if (!IsEmpty (req.getMylink ()) && ((new UrlValidator ()).isValid (req.getMylink ())))
        {
            mainAccount.setMyLink (req.getMylink ());
            req.setUpdatedMylink ();
        }

        if (!IsEmpty (req.getCodeConfirmPhone ()))
        {
            Confirmed confirmed
                    = confirmedService.getConfirmedIsActiveConfirmed (req.getCodeConfirmPhone ());
            if (confirmed != null)
            {
                String phone = mainAccount.getPhone ();
                if (phone.equals (confirmed.getValue ()))
                    req.setMessage (RequestMIU.Message.duplicate_phone_number);
                else
                {
                    confirmedService.deactive (mainAccount.getPhone () , ConfirmCodeFor.phone);
                    mainAccount.setPhone (confirmed.getValue ());
                    req.setUpdatePhone ();
                }
            }
        }
        mainAccount = Repository.save (mainAccount);

        if (!IsEmpty (req.getCodeEmail ()))
        {
            Confirmed confirmedEmail
                    = confirmedService.hasCodeFor (req.getCodeEmail () , ConfirmCodeFor.email);
            if (confirmedEmail != null)
            {
                if (confirmedEmail.getValue ().equals (mainAccount.getEmail ().getEmail ()))
                    req.setMessage (RequestMIU.Message.email_previously_added);
                else
                {
                    confirmedService.deactive (mainAccount.getEmail ().getEmail () , ConfirmCodeFor.email);

                    UserEmails userEmails = new UserEmails ();
                    userEmails.setMainAccount (mainAccount);
                    userEmails.setEmailFor (EmailFor.ma);
                    userEmails.setConfirmed (true);
                    userEmails.setConfirmedAt (confirmedEmail.getConfirmCode ().getTimeToConfirmed ());
                    userEmails.setEmail (confirmedEmail.getValue ());

                    userEmails = userEmailsService.Repository.save (userEmails);

                    mainAccount.setEmail (userEmails);

                    Repository.save (mainAccount);

                    req.setUpdateEmail ();
                }
            }
            else
                req.setMessage (RequestMIU.Message.email_confirmed_code_invalid);
        }

        return req;
    }

    public boolean hasEmail (long id)
    {
        return (Repository.findByIdAndEmailNotNullAndDeletedFalseAndSystemBlockFalseAndActiveTrue (id) != null);
    }

    public long toId (String username)
    {
        MainAccount byUsername = Repository.findByUsernameUsernameAndDeletedFalseAndSystemBlockFalseAndActiveTrue (username);
        if (byUsername == null) return 0;
        else return byUsername.getId ();
    }

    public MainAccount findId (long idUser)
    {
        return Repository.findByIdAndSystemBlockFalseAndActiveTrue (idUser);
    }

    public MainAccount findPhone (String phone)
    {
        return Repository.findByPhoneAndDeletedFalseAndSystemBlockFalseAndActiveTrue (phone);
    }

    public MainAccount findEmail (String email)
    {
        return Repository.findByEmailAndDeletedFalseAndSystemBlockFalseAndActiveTrue (email);
    }

    public MainAccount findPhone (String phone , String password)
    {
        return Repository.findByPhoneAndPasswordAndDeletedFalseAndSystemBlockFalseAndActiveTrue (phone , password);
    }

    public MainAccount findEmail (String email , String password)
    {
        return Repository.findByEmailAndPasswordAndDeletedFalseAndSystemBlockFalseAndActiveTrue (email , password);
    }

    public MainAccount findValidById (long id)
    {
        return Repository.findByIdAndDeletedFalseAndSystemBlockFalseAndActiveTrue (id);
    }

    public MainAccount findPhoneLike (String phone)
    {
        return Repository.findByPhoneLikeAndDeletedFalseAndSystemBlockFalseAndActiveTrue ("%" + phone);
    }

}
