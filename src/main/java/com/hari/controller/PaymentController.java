package com.hari.controller;

import com.hari.model.*;
import com.hari.response.ApiResponse;
import com.hari.response.PaymentLinkResponse;
import com.hari.service.*;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final TransactionService transactionService;

    @PostMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId  ,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt
    )throws  Exception , RazorpayException, StripeException {
        User user=userService.findUserByJwtToken(jwt);

        PaymentLinkResponse paymentResponse;

        PaymentOrder paymentorder=paymentService.getPaymentOrderByPaymentId(paymentLinkId);

        boolean paymentSuccess=paymentService.ProceedPaymentOrder(
                paymentorder,
                paymentId,
                paymentLinkId
        );
        if (paymentSuccess){
            for (Order order:paymentorder.getOrders()){
                transactionService.createTransaction(order);
                Seller seller=sellerService.getSellerById(order.getSellerId());
                SellerReport report=sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders()+1);
                report.setTotalEarnings(report.getTotalEarnings()+order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }

        }
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage("Payment success");

        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);
    }
}
