package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapRead;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GapReadRepository extends JpaRepository <GapRead, Long>
{
    List <GapRead> findByGapsToUserIdAndGapsFromIdAndReadIsFalse (long to , long from);
}
