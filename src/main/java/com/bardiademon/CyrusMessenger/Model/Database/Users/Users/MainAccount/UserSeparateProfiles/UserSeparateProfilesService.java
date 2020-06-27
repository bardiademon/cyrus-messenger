package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles;

import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class UserSeparateProfilesService
{
    public final UserSeparateProfilesRepository Repository;
    private final EnumTypesService enumTypesService;

    @Autowired
    public UserSeparateProfilesService (UserSeparateProfilesRepository Repository , EnumTypesService _EnumTypesService)
    {
        this.Repository = Repository;
        this.enumTypesService = _EnumTypesService;
    }

    /*
     *  baraye fahmidan inke id darkhasti baraye user darkhasti hast ya na
     */
    public UserSeparateProfiles forUser (long id , long idUser)
    {
        return Repository.findByIdAndMainAccountIdAndDeletedFalse (id , idUser);
    }

    public UserSeparateProfiles getSeparateProfiles (AccessLevel.Who who , MainAccount mainAccount)
    {
        List <UserSeparateProfiles> separate = Repository.findByMainAccountIdAndDeletedFalse (mainAccount.getId ());
        for (UserSeparateProfiles separateProfiles : separate)
        {
            if (enumTypesService.Repository.findById2AndEnumTypeAndDeletedFalse (separateProfiles.getId () , who.name ()) != null)
                return separateProfiles;
        }
        return null;
    }

    public List <IdEnTy> findIdType (long idUser)
    {
        List <Object[]> idSeparateProfiles = Repository.findIdSeparateProfiles (idUser);
        if (idSeparateProfiles != null && idSeparateProfiles.size () > 0)
        {
            List <IdEnTy> idEntyLst = new ArrayList <> ();
            for (Object[] idSeparateProfile : idSeparateProfiles)
            {
                IdEnTy idEnTy = new IdEnTy ();
                idEnTy.setEnumType (idSeparateProfile[0].toString ());
                idEnTy.setId (new ID (idSeparateProfile[1]));
                idEntyLst.add (idEnTy);
            }
            System.gc ();
            return idEntyLst;
        }
        else return null;
    }
}
