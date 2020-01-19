package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainAccountRepository extends JpaRepository<MainAccount, Long>
{

    MainAccount findByPhoneAndRegisteredTrueAndDeletedFalse (String phone);

    MainAccount findByPhoneAndRegisteredFalseAndDeletedFalse (String phone);

    MainAccount findByUsernameAndRegisteredTrueAndDeletedFalse (String username);

    MainAccount findByEmailAndRegisteredTrueAndDeletedFalse (String email);

    MainAccount findByPhoneAndPasswordAndRegisteredTrueAndDeletedFalse (String phone , String password);

    MainAccount findByUsernameAndPasswordAndRegisteredTrueAndDeletedFalse (String username , String password);

    MainAccount findByEmailAndPasswordAndRegisteredTrueAndDeletedFalse (String email , String password);

    MainAccount findByIdAndEmailNotNullAndRegisteredTrueAndDeletedFalse (long id);

}
