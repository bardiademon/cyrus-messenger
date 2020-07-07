package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainAccountRepository extends JpaRepository<MainAccount, Long>
{

    MainAccount findByPhoneAndDeletedFalseAndSystemBlockFalseAndActiveTrue (String phone);

    MainAccount findByPhoneLikeAndDeletedFalseAndSystemBlockFalseAndActiveTrue (String phone);

    MainAccount findByUsernameUsernameAndDeletedFalseAndSystemBlockFalseAndActiveTrue (String username);

    MainAccount findByEmailAndDeletedFalseAndSystemBlockFalseAndActiveTrue (String email);

    MainAccount findByPhoneAndPasswordAndDeletedFalseAndSystemBlockFalseAndActiveTrue (String phone , String password);

    MainAccount findByUsernameUsernameAndPasswordAndDeletedFalseAndSystemBlockFalseAndActiveTrue (String username , String password);

    MainAccount findByEmailAndPasswordAndDeletedFalseAndSystemBlockFalseAndActiveTrue (String email , String password);

    MainAccount findByIdAndEmailNotNullAndDeletedFalseAndSystemBlockFalseAndActiveTrue (long id);

    MainAccount findByIdAndDeletedFalseAndSystemBlockFalseAndActiveTrue (long id);

    MainAccount findByIdAndSystemBlockFalseAndActiveTrue (long id);

}
