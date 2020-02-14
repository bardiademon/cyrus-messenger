package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SecurityUserProfileRepository extends JpaRepository<SecurityUserProfile, Long>
{
    SecurityUserProfile findByMainAccount (MainAccount mainAccount);

    @Query ("select sec.maxUploadProfilePictures from SecurityUserProfile sec where sec.mainAccount.id = :ID_USER")
    int getMaxUploadProfilePictures (@Param ("ID_USER") long idUser);
}
