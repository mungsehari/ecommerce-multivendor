package com.hari.controller;

import com.hari.domain.USER_ROLE;
import com.hari.request.LoginOtpRequest;
import com.hari.request.LoginRequest;
import com.hari.request.SignupRequest;
import com.hari.response.ApiResponse;
import com.hari.response.AuthResponse;
import com.hari.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest request) throws Exception {
        String jwt = authService.createUser(request);
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("User created successfully");
        response.setRole(USER_ROLE.ROLE_CUSTOMER);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestBody LoginOtpRequest request) throws Exception {
        authService.sendLoginOtp(request.getEmail(), request.getRole());
        ApiResponse response = new ApiResponse();
        response.setMessage("OTP sent successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request) throws Exception {
        AuthResponse authResponse = authService.signing(request);
        ApiResponse response = new ApiResponse();
        response.setMessage("OTP sent successfully");
        return ResponseEntity.ok(authResponse);
    }
}
