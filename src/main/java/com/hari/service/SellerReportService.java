package com.hari.service;

import com.hari.model.Seller;
import com.hari.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport report);
}
