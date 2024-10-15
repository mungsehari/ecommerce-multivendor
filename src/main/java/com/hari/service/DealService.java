package com.hari.service;

import com.hari.model.Deal;

import java.util.List;

public interface DealService {
    List<Deal> getDeals();
    Deal createDeal(Deal deal) throws Exception;
    Deal updateDeal(Deal deal,Long id) throws Exception;
    void deleteDeal(Long id) throws Exception;
}
