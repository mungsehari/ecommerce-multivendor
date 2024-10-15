package com.hari.controller;

import com.hari.model.Deal;
import com.hari.response.ApiResponse;
import com.hari.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {
    private final DealService dealService;

    @PostMapping
    public ResponseEntity<Deal> createDeals(@RequestBody Deal deal) throws Exception {
        Deal createdDeal=dealService.createDeal(deal);
        return new ResponseEntity<>(createdDeal, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(@PathVariable Long id, @RequestBody Deal deal) throws Exception {

        Deal updatedDeal=dealService.updateDeal(deal,id);
        return new ResponseEntity<>(updatedDeal,HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeal(@PathVariable Long id) throws Exception {
        dealService.deleteDeal(id);
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage("deal deleted successfully");
        return new ResponseEntity<>(apiResponse,HttpStatus.ACCEPTED);
    }
}
