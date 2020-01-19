package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.New.General.RequestGeneral;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestRegister.RegisterRequest;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChat;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChatRepository;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileRepository;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor.ShowChatFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor.ShowChatForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.bardiademon.Hash256;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainAccountService
{

    public final MainAccountRepository Repository;
    private SecurityUserChatRepository repositorySecurityChat;
    private SecurityUserProfileRepository repositorySecurityProfile;
    private ShowProfileForService showProfileForService;
    private ShowChatForService showChatForService;

    @Autowired
    public MainAccountService (MainAccountRepository Repository ,
                               SecurityUserChatRepository RepositorySecurityChat ,
                               SecurityUserProfileRepository RepositorySecurityProfile ,
                               ShowProfileForService _ShowProfileForService ,
                               ShowChatForService _ShowChatForService
    )
    {
        this.Repository = Repository;
        this.repositorySecurityChat = RepositorySecurityChat;
        this.repositorySecurityProfile = RepositorySecurityProfile;
        this.showProfileForService = _ShowProfileForService;
        this.showChatForService = _ShowChatForService;
    }


    public boolean newAccount (RegisterRequest registerRequest)
    {
        MainAccount mainAccount = new MainAccount ();
        mainAccount.setName (registerRequest.name);
        mainAccount.setFamily (registerRequest.family);
        mainAccount.setPhone (registerRequest.getPhone ());
        mainAccount.setUsername (registerRequest.username);
        mainAccount.setPassword ((new Hash256 ()).hash (registerRequest.password));

        MainAccount save = Repository.save (mainAccount);
        if (save != null)
        {
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

            return true;
        }
        else return false;
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

        if (!req.isNull (req.getUsername ()) && findUsername (req.getUsername ()) == null)
        {
            mainAccount.setUsername (req.getUsername ());
            req.setUpdatedUsername ();
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
        return (Repository.findByIdAndEmailNotNullAndRegisteredTrueAndDeletedFalse (id) != null);
    }

    public long toId (String username)
    {
        MainAccount byUsername = Repository.findByUsernameAndRegisteredTrueAndDeletedFalse (username);
        if (byUsername == null) return 0;
        else return byUsername.getId ();
    }

    public MainAccount findPhone (String phone)
    {
        return Repository.findByPhoneAndRegisteredTrueAndDeletedFalse (phone);
    }

    public MainAccount findUsername (String username)
    {
        return Repository.findByUsernameAndRegisteredTrueAndDeletedFalse (username);
    }

    public MainAccount findEmail (String email)
    {
        return Repository.findByEmailAndRegisteredTrueAndDeletedFalse (email);
    }

    public MainAccount findPhone (String phone , String password)
    {
        return Repository.findByPhoneAndPasswordAndRegisteredTrueAndDeletedFalse (phone , password);
    }

    public MainAccount findUsername (String username , String password)
    {
        return Repository.findByUsernameAndPasswordAndRegisteredTrueAndDeletedFalse (username , password);
    }

    public MainAccount findEmail (String email , String password)
    {
        return Repository.findByEmailAndPasswordAndRegisteredTrueAndDeletedFalse (email , password);
    }

    public MainAccount findId (long id)
    {
        return Repository.findByIdAndEmailNotNullAndRegisteredTrueAndDeletedFalse (id);
    }

}
