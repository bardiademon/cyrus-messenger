package com.bardiademon.CyrusMessenger.Model.Database.Default;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultRepository extends JpaRepository <Default, Long>
{
    Default findByKeyAndTypeValue (DefaultKey key , DefaultType defaultType);

    Default findByKey (DefaultKey key);
}
