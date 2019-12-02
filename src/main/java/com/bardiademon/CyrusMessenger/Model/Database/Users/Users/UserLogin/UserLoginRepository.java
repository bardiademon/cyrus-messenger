package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Long>
{
}
