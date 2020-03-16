package com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ProfilePicturesService
{
    public final ProfilePicturesRepository Repository;

    @Autowired
    public ProfilePicturesService (ProfilePicturesRepository Repository)
    {
        this.Repository = Repository;
    }

    public int countUploadPicUser (long idUser)
    {
        return Repository.countByDeletedFalseAndThisPicForAndMainAccountId (ProfilePicFor.user , idUser);
    }

    public int countUploadPicGroup (long idGroup)
    {
        return Repository.countByDeletedFalseAndThisPicForAndGroupsId (ProfilePicFor.group , idGroup);
    }

    public void disableMainPhotoUser (long id)
    {
        Repository.disableMainPhotoUser (id , ProfilePicFor.user);
    }

    public void disableMainPhotoGroup (long id)
    {
        Repository.disableMainPhotoGroup (id , ProfilePicFor.group);
    }

    public ProfilePictures getOneForUser (long idProfilePicture , long idUser)
    {
        return getOne (idProfilePicture , idUser , ProfilePicFor.user);
    }

    public ProfilePictures getOne (long idProfilePicture , long idUser , ProfilePicFor profilePicFor)
    {
        return Repository.findByIdAndMainAccountIdAndDeletedFalseAndThisPicFor (idProfilePicture , idUser , profilePicFor);
    }

    public ProfilePictures getOneGroup (long idProfilePicture , long idGroup)
    {
        return Repository.findByIdAndGroupsIdAndDeletedFalseAndThisPicFor (idProfilePicture , idGroup , ProfilePicFor.group);
    }

    public ProfilePictures getOneForUser (long idProfilePicture)
    {
        return Repository.findByIdAndThisPicForAndDeletedFalse (idProfilePicture , ProfilePicFor.user);
    }

    public int deletePlacementNumberZero (long idUser , boolean deleteMainPic)
    {
        return Repository.deletePlacementNumberZero (idUser , deleteMainPic);
    }

    public int deletePlacementNumberNotZero (long idUser , boolean deleteMainPic)
    {
        return Repository.deletePlacementNumberNotZero (idUser , deleteMainPic);
    }

    public int deleteMainPic (long idUser)
    {
        return Repository.deleteMainPic (idUser);
    }

}
