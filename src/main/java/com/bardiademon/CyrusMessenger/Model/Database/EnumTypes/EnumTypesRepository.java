package com.bardiademon.CyrusMessenger.Model.Database.EnumTypes;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnumTypesRepository extends JpaRepository <EnumTypes, Long>
{
    List <EnumTypes> findById2AndDeletedFalseAndDes (long id2 , String des);

    List <EnumTypes> findById2AndDeletedFalse (long id2);

    EnumTypes findById2AndEnumTypeAndDeletedFalse (long id2 , String enumType);

    @Query ("select enty.id,enty.enumType from EnumTypes enty where enty.id2 = :ID2 and enty.deleted = false")
    List <Object[]> getEnumType (@Param ("ID2") long id2);

    @Query ("select enty.id from EnumTypes enty where enty.id = :ID and enty.deleted = false")
    Long findId (@Param ("ID") long idEnty);

    @Query ("select enty.id from EnumTypes enty where enty.id = :ID and enty.id2 = :ID2 and enty.deleted = false")
    Long findId (@Param ("ID") long idEnty , @Param ("ID2") long id2);

    @Transactional
    @Modifying
    @Query ("update EnumTypes enty set enty.enumType = :ENUM_TYPE where enty.id = :ID and enty.id2 = :ID2 and enty.deleted = false")
    void updateEnumType (@Param ("ENUM_TYPE") String enumType , @Param ("ID") long id , @Param ("ID2") long id2);
}
