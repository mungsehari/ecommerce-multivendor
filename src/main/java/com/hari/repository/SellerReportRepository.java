package com.hari.repository;

import com.hari.model.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerReportRepository extends JpaRepository<SellerReport, Long> {
    SellerReport findSellerId(Long sellerId);
}
