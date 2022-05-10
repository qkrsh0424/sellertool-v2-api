package com.sellertool.server.domain.erp_download_excel_header.repository;

import com.sellertool.server.domain.erp_download_excel_header.entity.ErpDownloadExcelHeaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ErpDownloadExcelHeaderRepository extends JpaRepository<ErpDownloadExcelHeaderEntity, Integer> {
    Optional<ErpDownloadExcelHeaderEntity> findById(UUID headerId);
}
