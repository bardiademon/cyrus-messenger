package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GapsRepository extends JpaRepository <Gaps, Long>
{
    Gaps findById (long id);
}
