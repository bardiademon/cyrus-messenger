package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapRead;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GapReadRepository extends JpaRepository <GapRead, Long>
{
}
