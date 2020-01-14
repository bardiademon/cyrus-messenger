package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Long>
{
    UserLogin findByCodeLogin (String code);

    @Query ("select userLogin from UserLogin userLogin where userLogin.codeLogin=:code and userLogin.creditUp>:time and userLogin.timeLogout=null")
    UserLogin findValidCode (@Param ("code") String code , @Param ("time") LocalDateTime time);
}
