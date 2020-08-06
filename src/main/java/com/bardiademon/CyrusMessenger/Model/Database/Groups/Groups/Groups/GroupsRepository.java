package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long>
{
    int countByOwnerIdAndDeletedFalse (long id);

    Groups findByGroupnameAndDeletedFalse (String username);

    Groups findByLinkForJoinIdAndDeletedFalse (long id);

    Groups findById (long id);

    List<Groups> findByOwnerId (long idUser);
}
