package com.bardiademon.CyrusMessenger.Model.Database.LinkForJoin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkForJoinRepository extends JpaRepository<LinkForJoin, Long>
{
    LinkForJoin findByLinkAndDeletedFalse (String link);

    LinkForJoin findByLinkAndDeletedFalseAndGroupsNotNull (String link);
}
