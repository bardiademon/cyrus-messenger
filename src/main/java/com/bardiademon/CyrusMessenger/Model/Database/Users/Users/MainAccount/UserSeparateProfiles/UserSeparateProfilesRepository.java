package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSeparateProfilesRepository extends JpaRepository <UserSeparateProfiles, Long>
{

    List <UserSeparateProfiles> findByMainAccountIdAndDeletedFalse (long idUser);

    UserSeparateProfiles findByIdAndMainAccountIdAndDeletedFalse (long id , long idUser);

    @Query ("select enty.enumType,sep.id from EnumTypes enty,UserSeparateProfiles sep where sep.mainAccount.id = :ID_USER and enty.id2 = sep.id")
    List <Object[]> findIdSeparateProfiles (@Param ("ID_USER") long idUser);
}
