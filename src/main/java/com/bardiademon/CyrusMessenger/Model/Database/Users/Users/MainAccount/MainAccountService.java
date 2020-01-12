package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import com.bardiademon.CyrusMessenger.Controller.Rest.RestRegister.RegisterRequest;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChat;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChatRepository;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileRepository;
import com.bardiademon.CyrusMessenger.bardiademon.Hash256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainAccountService
{

    public final MainAccountRepository Repository;
    private SecurityUserChatRepository repositorySecurityChat;
    private SecurityUserProfileRepository repositorySecurityProfile;

    @Autowired
    public MainAccountService (MainAccountRepository Repository ,
                               SecurityUserChatRepository RepositorySecurityChat ,
                               SecurityUserProfileRepository RepositorySecurityProfile)
    {
        this.Repository = Repository;
        this.repositorySecurityChat = RepositorySecurityChat;
        this.repositorySecurityProfile = RepositorySecurityProfile;
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
            System.out.println (save.getId ());


            SecurityUserProfile securityUserProfile = new SecurityUserProfile ();
            securityUserProfile.setMainAccount (save);

            SecurityUserChat securityUserChat = new SecurityUserChat ();
            securityUserChat.setMainAccount (save);

            SecurityUserProfile newSecurityUserProfile = repositorySecurityProfile.save (securityUserProfile);
            SecurityUserChat newSecurityUserChat = repositorySecurityChat.save (securityUserChat);

            return newSecurityUserChat != null && newSecurityUserProfile != null;
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
