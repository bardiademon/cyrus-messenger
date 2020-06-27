package com.bardiademon.CyrusMessenger.Model.Database.EnumTypes;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
