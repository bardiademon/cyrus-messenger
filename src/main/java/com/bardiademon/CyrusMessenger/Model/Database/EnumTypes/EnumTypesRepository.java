package com.bardiademon.CyrusMessenger.Model.Database.EnumTypes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnumTypesRepository extends JpaRepository <EnumTypes, Long>
{
    List <EnumTypes> findById2AndDeletedFalseAndDes (long id2 , String des);
}