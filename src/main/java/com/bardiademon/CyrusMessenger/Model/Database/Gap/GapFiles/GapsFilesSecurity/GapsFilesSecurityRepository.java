package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFilesSecurity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GapsFilesSecurityRepository extends JpaRepository <GapsFilesSecurity, Long>
{

}
