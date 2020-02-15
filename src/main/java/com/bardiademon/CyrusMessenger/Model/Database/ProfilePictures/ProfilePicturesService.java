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

    public int countUploadPic (ProfilePicFor picFor)
    {
        return Repository.countByDeletedFalseAndThisPicFor (picFor);
    }

    public void disableMainPhoto (long id)
    {
        Repository.disableMainPhoto (id);
    }

    public ProfilePictures getOneForUser (long idProfilePicture , long idUser)
    {
        return Repository.findByIdAndMainAccountIdAndDeletedFalse (idProfilePicture , idUser);
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
