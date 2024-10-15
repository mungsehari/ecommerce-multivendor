package com.hari.controller;

import com.hari.domain.AccountStatus;
import com.hari.exceptions.SellerException;
import com.hari.model.Seller;
import com.hari.model.SellerReport;
import com.hari.model.VerificationCode;
import com.hari.repository.VerificationCodeRepository;
import com.hari.request.LoginRequest;
import com.hari.response.AuthResponse;
import com.hari.service.AuthService;
import com.hari.service.EmailService;
import com.hari.service.SellerReportService;
import com.hari.service.SellerService;
import com.hari.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final SellerReportService sellerReportService;



    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(
            @RequestBody LoginRequest request
            ) throws Exception {
        String otp=request.getOtp();
        String email=request.getEmail();

        request.setEmail("seller_"+email);
        AuthResponse authResponse=authService.signing(request);
        return ResponseEntity.ok(authResponse);

    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {

        VerificationCode verificationCode=verificationCodeRepository.findByOtp(otp);
        if (verificationCode==null||!verificationCode.getOtp().equals(otp)){
            throw new Exception("Wrong OTP");
        }
        Seller seller=sellerService.verifyEmail(verificationCode.getEmail(),otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {
        Seller savedSeller=sellerService.createSeller(seller);

        String otp= OtpUtil.generateOtp();

        VerificationCode verificationCode=new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject="CodeMitra Bazaar Email Verification Code";
        String text="Welcome to CodeMitra Bazaar, your account using this link  ";
        String frontend_url="http://localhost:3000/verfiy-seller";
        emailService.sendVerificationOtpEmail(seller.getEmail(),verificationCode.getOtp(),subject,text+frontend_url);
        return new ResponseEntity<>(savedSeller,HttpStatus.CREATED);

    }
    @GetMapping("/{id}")
     public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
         Seller seller=sellerService.getSellerById(id);
         return new ResponseEntity<>(seller,HttpStatus.OK);
     }

     @GetMapping("/profile")
     public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller=sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller,HttpStatus.OK);
     }

     @GetMapping("/report")
     public ResponseEntity<SellerReport> getSellerReport(
             @RequestHeader("Authorization") String jwt
     ) throws Exception {

        Seller seller=sellerService.getSellerProfile(jwt);
        SellerReport sellerReport=sellerReportService.getSellerReport(seller);

        return new ResponseEntity<>(sellerReport,HttpStatus.OK);

     }

     @GetMapping
     public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false)AccountStatus status){
        List<Seller> sellers=sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
     }

     @PatchMapping
     public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller) throws Exception {
        Seller profile=sellerService.getSellerProfile(jwt);
        Seller updatedSeller=sellerService.updateSeller(profile.getId(),seller);
        return ResponseEntity.ok(updatedSeller);
     }
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
     }


}
