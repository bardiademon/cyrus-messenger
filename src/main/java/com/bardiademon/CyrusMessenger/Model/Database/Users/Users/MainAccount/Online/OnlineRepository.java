package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Online;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineRepository extends JpaRepository <Online, Long>
{
}
