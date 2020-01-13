package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

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
        mainAccount.setEmail (registerRequest.email);
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

    public long toId (String username)
    {
        MainAccount byUsername = Repository.findByUsername (username);
        if (byUsername == null) return 0;
        else return byUsername.getId ();
    }

}
