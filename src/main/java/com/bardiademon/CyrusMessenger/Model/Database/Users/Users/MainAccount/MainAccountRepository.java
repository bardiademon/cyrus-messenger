package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainAccountRepository extends JpaRepository<MainAccount, Long>
{

    MainAccount findByPhone (String phone);

    MainAccount findByUsername (String username);

    MainAccount findByEmail (String email);

    MainAccount findByPhoneAndPassword (String phone , String password);

    MainAccount findByUsernameAndPassword (String username , String password);

    MainAccount findByEmailAndPassword (String email , String password);

    MainAccount findByIdAndStatusNot (long id , MainAccountStatus status);


}
