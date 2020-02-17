package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainAccountRepository extends JpaRepository<MainAccount, Long>
{

    MainAccount findByPhoneAndDeletedFalseAndSystemBlockFalse (String phone);

    MainAccount findByUsernameAndDeletedFalseAndSystemBlockFalse (String username);

    MainAccount findByEmailAndDeletedFalseAndSystemBlockFalse (String email);

    MainAccount findByPhoneAndPasswordAndDeletedFalseAndSystemBlockFalse (String phone , String password);

    MainAccount findByUsernameAndPasswordAndDeletedFalseAndSystemBlockFalse (String username , String password);

    MainAccount findByEmailAndPasswordAndDeletedFalseAndSystemBlockFalse (String email , String password);

    MainAccount findByIdAndEmailNotNullAndDeletedFalseAndSystemBlockFalse (long id);

    MainAccount findByIdAndDeletedFalseAndSystemBlockFalse (long id);

    MainAccount findByPhoneLikeAndSystemBlockFalse (String phone);

    MainAccount findByIdAndSystemBlockFalse (long id);

}
