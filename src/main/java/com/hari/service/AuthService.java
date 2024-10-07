package com.hari.service;

import com.hari.request.LoginRequest;
import com.hari.request.SignupRequest;
import com.hari.response.AuthResponse;

public interface AuthService {
    void sendLoginOtp(String email) throws Exception;
    String createUser(SignupRequest request) throws Exception;
    AuthResponse signing(LoginRequest request);
}
