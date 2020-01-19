package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainAccountRepository extends JpaRepository<MainAccount, Long>
{

    MainAccount findByPhoneAndDeletedFalse (String phone);

    MainAccount findByUsernameAndDeletedFalse (String username);

    MainAccount findByEmailAndDeletedFalse (String email);

    MainAccount findByPhoneAndPasswordAndDeletedFalse (String phone , String password);

    MainAccount findByUsernameAndPasswordAndDeletedFalse (String username , String password);

    MainAccount findByEmailAndPasswordAndDeletedFalse (String email , String password);

    MainAccount findByIdAndEmailNotNullAndDeletedFalse (long id);

    MainAccount findByPhoneAndRegisteredFalseAndDeletedFalse (String phone);


}
