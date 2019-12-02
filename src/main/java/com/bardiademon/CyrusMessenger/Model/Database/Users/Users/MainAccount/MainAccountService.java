package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import com.bardiademon.CyrusMessenger.Controller.RestRegister.RegisterRequest;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChat;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChatRepository;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        mainAccount.setPassword (((PasswordEncoder) new BCryptPasswordEncoder ()).encode (registerRequest.password));

        MainAccount save = Repository.save (mainAccount);

        if (save != null)
        {
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

}
