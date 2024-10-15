package com.hari.service.Impl;

import com.hari.config.JwtProvider;
import com.hari.domain.USER_ROLE;
import com.hari.model.Cart;
import com.hari.model.Seller;
import com.hari.model.User;
import com.hari.model.VerificationCode;
import com.hari.repository.CartRepository;
import com.hari.repository.SellerRepository;
import com.hari.repository.UserRepository;
import com.hari.repository.VerificationCodeRepository;
import com.hari.request.LoginRequest;
import com.hari.request.SignupRequest;
import com.hari.response.AuthResponse;
import com.hari.service.AuthService;
import com.hari.service.EmailService;
import com.hari.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final CartRepository cartRepository;

    private final VerificationCodeRepository verificationCodeRepository;

    private final JwtProvider jwtProvider;

    private final EmailService emailService;

    private final CustomerUserDetailsServiceImpl customerUserDetailsService;
    private final SellerRepository sellerRepository;

    @Override
    public void sendLoginOtp(String email,USER_ROLE role) throws Exception {
        String SIGNING_PREFIX="signing_";

        if ( email.startsWith(SIGNING_PREFIX)){
            email=email.substring(SIGNING_PREFIX.length());

            if (role.equals(USER_ROLE.ROLE_SELLER)){
                Seller seller=sellerRepository.findByEmail(email);
                if (seller==null){
                    throw new Exception("Seller not found ");
                }

            }else {
                User user=userRepository.findByEmail(email);
                if (user==null){
                    throw new Exception("User not exist with provided email");
                }
            }

        }

        VerificationCode isExist=verificationCodeRepository.findByEmail(email);
        if (isExist != null) {

            verificationCodeRepository.delete(isExist);
        }
        String otp= OtpUtil.generateOtp();
        VerificationCode verificationCode=new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject="Hari bazaar login/signup otp";
        String text="Your login/signup otp is -" + otp;

        emailService.sendVerificationOtpEmail(email,otp,subject,text);
    }

    @Override
    public String createUser(SignupRequest request) throws Exception {

        VerificationCode verificationCode=verificationCodeRepository.findByEmail(request.getEmail());
        if (verificationCode==null || !verificationCode.getOtp().equals(request.getOtp())){

            throw new Exception("Wrong OTP");
        }


        User user=userRepository.findByEmail(request.getEmail());
        if (user==null){
          User createUser=new User();
          createUser.setEmail(request.getEmail());
          createUser.setFullName(request.getFullName());
          createUser.setRole(USER_ROLE.ROLE_CUSTOMER);
          createUser.setMobile("9309019005");
          createUser.setPassword(passwordEncoder.encode(request.getOtp()));
         user=userRepository.save(createUser);
            Cart cart=new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signing(LoginRequest request) throws Exception {
        String username=request.getEmail();
        String otp=request.getOtp();
        Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse response=new AuthResponse();
        response.setJwt(token);
        response.setMessage("Login successfully");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
        response.setRole(USER_ROLE.valueOf(roleName));

        return response;
    }

    private Authentication authenticate(String username, String otp) throws Exception {
      UserDetails userDetails= customerUserDetailsService.loadUserByUsername(username);
        String SELLER_PREFIX = "seller_";
        if (username.startsWith(SELLER_PREFIX)) {

           username=username.substring(SELLER_PREFIX.length());
        }
      if (userDetails==null){
          throw new BadCredentialsException("invalid username ");
      }
      VerificationCode verificationCode=verificationCodeRepository.findByEmail(username);
      if (verificationCode==null || !verificationCode.getOtp().equals(otp)){
          throw new Exception("Wrong OTP");
      }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
