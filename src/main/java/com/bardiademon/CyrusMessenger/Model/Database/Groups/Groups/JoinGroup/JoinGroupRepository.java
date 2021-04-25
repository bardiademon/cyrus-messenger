package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinGroupRepository extends JpaRepository <JoinGroup, Long>
{
    JoinGroup findByGroupsIdAndMainAccountIdAndLeaveGroupFalse (long idGroup , long idUser);

    List <JoinGroup> findByMainAccountIdAndLeaveGroupFalse (long id);
}
