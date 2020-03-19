package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.New.General.RequestGeneral;
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
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCode;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ConfirmedPhone.ConfirmedPhone;
import com.bardiademon.CyrusMessenger.bardiademon.Hash256;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MainAccountService
{

    public final MainAccountRepository Repository;
    public final SecurityUserChatRepository repositorySecurityChat;
    public final SecurityUserProfileRepository repositorySecurityProfile;
    public final ShowProfileForService showProfileForService;
    public final ShowChatForService showChatForService;
    public final UsernamesService usernamesService;

    @Autowired
    public MainAccountService
            (MainAccountRepository Repository ,
             SecurityUserChatRepository RepositorySecurityChat ,
             SecurityUserProfileRepository RepositorySecurityProfile ,
             ShowProfileForService _ShowProfileForService ,
             ShowChatForService _ShowChatForService ,
             UsernamesService _UsernamesService
            )
    {
        this.Repository = Repository;
        this.repositorySecurityChat = RepositorySecurityChat;
        this.repositorySecurityProfile = RepositorySecurityProfile;
        this.showProfileForService = _ShowProfileForService;
        this.showChatForService = _ShowChatForService;
        this.usernamesService = _UsernamesService;
    }


    public boolean newAccount (RegisterRequest registerRequest , ConfirmedPhone confirmedPhone , ConfirmCodeService confirmCodeService)
    {
        MainAccount mainAccount = new MainAccount ();
        mainAccount.setName (registerRequest.getName ());
        mainAccount.setFamily (registerRequest.getFamily ());
        mainAccount.setPhone (confirmedPhone.getPhone ());

        Usernames usernames = new Usernames ();
        usernames.setUsername (registerRequest.getUsername ());
        usernames.setUsernameFor (UsernameFor.user);
        usernames = usernamesService.Repository.save (usernames);

        mainAccount.setUsername (usernames);
        mainAccount.setPassword ((new Hash256 ()).hash (registerRequest.getPassword ()));

        MainAccount save = Repository.save (mainAccount);

        usernames.setMainAccount (save);
        usernamesService.Repository.save (usernames);

        SecurityUserProfile securityUserProfile = new SecurityUserProfile ();
        securityUserProfile.setMainAccount (save);

        SecurityUserChat securityUserChat = new SecurityUserChat ();
        securityUserChat.setCanSendNumberOfMessageUnread (0);
        securityUserChat.setMainAccount (save);

        SecurityUserProfile newSecurityUserProfile = repositorySecurityProfile.save (securityUserProfile);
        SecurityUserChat newSecurityUserChat = repositorySecurityChat.save (securityUserChat);

        ShowProfileFor showProfileFor = new ShowProfileFor ();
        showProfileFor.setSecurityUserProfile (newSecurityUserProfile);

        ShowChatFor showChatFor = new ShowChatFor ();
        showChatFor.setSecurityUserChat (newSecurityUserChat);

        showProfileForService.Repository.save (showProfileFor);
        showChatForService.Repository.save (showChatFor);


        ConfirmCode confirmCode = confirmedPhone.getConfirmCode ();
        confirmCode.setMainAccount (mainAccount);
        confirmCodeService.Repository.save (confirmCode);

        return mainAccount.getId () > 0;

    }

    public RequestGeneral updateGeneral (MainAccount mainAccount , RequestGeneral req)
    {
        if (!req.thereIsAtLeastOneTrue ()) return null;

        if (!req.isNull (req.getBio ()))
        {
            mainAccount.setBio (req.getBio ());
            req.setUpdatedBio ();
        }

        if (!req.isNull (req.getName ()))
        {
            mainAccount.setName (req.getName ());
            req.setUpdatedName ();
        }

        if (!req.isNull (req.getFamily ()))
        {
            mainAccount.setFamily (req.getFamily ());
            req.setUpdatedFamily ();
        }

        if (!req.isNull (req.getMylink ()) && ((new UrlValidator ()).isValid (req.getMylink ())))
        {
            mainAccount.setMyLink (req.getMylink ());
            req.setUpdatedMylink ();
        }

        Repository.save (mainAccount);
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
        return Repository.findByPhoneLikeAndSystemBlockFalseAndActiveTrue ("%" + phone);
    }

}
