package com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ProfilePicturesRepository extends JpaRepository<ProfilePictures, Long>
{
    int countByDeletedFalseAndThisPicFor (ProfilePicFor picFor);

    @Modifying
    @Transactional
    @Query ("update ProfilePictures profilePictures set profilePictures.mainPic = false where profilePictures.mainAccount.id = :ID")
    void disableMainPhoto (@Param ("ID") long id);

    ProfilePictures findByIdAndDeletedFalse (long id);
}
