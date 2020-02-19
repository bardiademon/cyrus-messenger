package com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
public interface ProfilePicturesRepository extends JpaRepository<ProfilePictures, Long>
{
    int countByDeletedFalseAndThisPicFor (ProfilePicFor picFor);

    @Modifying
    @Transactional
    @Query ("update ProfilePictures profilePictures set profilePictures.mainPic = false where profilePictures.mainAccount.id = :ID and profilePictures.deleted = false")
    void disableMainPhoto (@Param ("ID") long id);

    ProfilePictures findByIdAndDeletedFalse (long id);

    ProfilePictures findByIdAndMainAccountIdAndDeletedFalse (long id , long idUser);

    @Modifying
    @Transactional
    @Query ("update ProfilePictures profilePictures set profilePictures.deleted = true , profilePictures.deletedAt = CURRENT_TIMESTAMP where  profilePictures.mainAccount.id = :ID_USER and ((profilePictures.placementNumber = 0 and profilePictures.mainPic = false) or (profilePictures.placementNumber = 0 and profilePictures.mainPic = :MAIN_PIC))")
    Integer deletePlacementNumberZero (@Param ("ID_USER") long idUser , @Param ("MAIN_PIC") boolean mainPic);

    @Modifying
    @Transactional
    @Query ("update ProfilePictures profilePictures set profilePictures.deleted = true , profilePictures.deletedAt = CURRENT_TIMESTAMP where  profilePictures.mainAccount.id = :ID_USER and profilePictures.mainPic = true")
    Integer deleteMainPic (@Param ("ID_USER") long idUser);

    @Modifying
    @Transactional
    @Query ("update ProfilePictures profilePictures set profilePictures.deleted = true , profilePictures.deletedAt = CURRENT_TIMESTAMP where  profilePictures.mainAccount.id = :ID_USER and (profilePictures.placementNumber != 0 and profilePictures.mainPic = false) or (profilePictures.placementNumber != 0 and profilePictures.mainPic = :MAIN_PIC)")
    Integer deletePlacementNumberNotZero (@Param ("ID_USER") long idUser , @Param ("MAIN_PIC") boolean mainPic);

    @Query ("select count(profilePictures) from ProfilePictures profilePictures where  profilePictures.groups.id = :ID_GROUP and profilePictures.thisPicFor = 'group'")
    Integer countUploadGroup (@Param ("ID_GROUP") long idGroup);

    @Query ("select count(profilePictures) from ProfilePictures profilePictures where  profilePictures.mainAccount.id = :ID_USER and profilePictures.thisPicFor = 'user'")
    Integer countUploadUser (@Param ("ID_USER") long idUser);

}
