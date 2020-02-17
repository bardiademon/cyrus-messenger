package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupSecurityProfileRepository extends JpaRepository<GroupSecurityProfile, Long>
{
    GroupSecurityProfile findByGroupsId (long id);

}
