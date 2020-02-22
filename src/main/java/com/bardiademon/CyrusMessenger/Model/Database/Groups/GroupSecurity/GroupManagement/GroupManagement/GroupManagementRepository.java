package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupManagementRepository extends JpaRepository<GroupManagement, Long>
{
    GroupManagement findByMainAccountIdAndGroupsIdAndSuspendedFalse (long idUser , long idGroup);
}
