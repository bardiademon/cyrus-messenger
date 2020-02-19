package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.FiredFromGroup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FiredFromGroupRepository extends JpaRepository<FiredFromGroup, Long>
{
    FiredFromGroup findByGroupIdAndMainAccountIdAndFreedFalse (long groupId , long idUser);
}
