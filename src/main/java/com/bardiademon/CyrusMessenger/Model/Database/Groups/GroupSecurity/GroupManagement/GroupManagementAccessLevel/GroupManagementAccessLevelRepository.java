package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagementAccessLevel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupManagementAccessLevelRepository extends JpaRepository<GroupManagementAccessLevel, Long>
{
}
