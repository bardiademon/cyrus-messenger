package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinGroupRepository extends JpaRepository<JoinGroup, Long>
{
    JoinGroup findByMainAccountIdAndLeaveGroupFalse (long id);
}
