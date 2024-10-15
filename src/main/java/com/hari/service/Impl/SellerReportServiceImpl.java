package com.hari.service.Impl;

import com.hari.model.Seller;
import com.hari.model.SellerReport;
import com.hari.repository.SellerReportRepository;
import com.hari.service.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {
    private final SellerReportRepository sellerReportRepository;


    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport sr=sellerReportRepository.findSellerId(seller.getId());
        if (sr==null){
            SellerReport newReport=new SellerReport();
            newReport.setSeller(seller);
            return sellerReportRepository.save(newReport);


        }
        return sr;

    }

    @Override
    public SellerReport updateSellerReport(SellerReport report) {
        return sellerReportRepository.save(report);
    }
}
