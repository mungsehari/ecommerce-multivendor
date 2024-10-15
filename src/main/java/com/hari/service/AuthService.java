package com.hari.service;

import com.hari.domain.USER_ROLE;
import com.hari.request.LoginRequest;
import com.hari.request.SignupRequest;
import com.hari.response.AuthResponse;

public interface AuthService {
    void sendLoginOtp(String email, USER_ROLE role) throws Exception;
    String createUser(SignupRequest request) throws Exception;
    AuthResponse signing(LoginRequest request) throws Exception;
}
