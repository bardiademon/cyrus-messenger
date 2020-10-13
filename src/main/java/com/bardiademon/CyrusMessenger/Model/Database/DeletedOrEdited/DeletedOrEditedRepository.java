package com.bardiademon.CyrusMessenger.Model.Database.DeletedOrEdited;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletedOrEditedRepository extends JpaRepository <DeletedOrEdited, Long>
{
}
