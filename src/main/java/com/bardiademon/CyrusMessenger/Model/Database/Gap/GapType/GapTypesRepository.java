package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GapTypesRepository extends JpaRepository <GapTypes, Long>
{
}
