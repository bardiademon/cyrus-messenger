package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFilesUsageReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GapsFilesUsageReportRepository extends JpaRepository <GapsFilesUsageReport, Long>
{
}
