package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long>
{
    int countByOwnerIdAndDeletedFalse (long id);

    Groups findByUsernameAndDeletedFalse (String username);

    Groups findByLinkForJoinIdAndDeletedFalse (long id);
}
