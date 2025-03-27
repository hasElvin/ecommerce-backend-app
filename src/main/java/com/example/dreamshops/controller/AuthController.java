package com.example.dreamshops.controller;

import com.example.dreamshops.request.LoginRequest;
import com.example.dreamshops.response.ApiResponse;
import com.example.dreamshops.response.JwtResponse;
import com.example.dreamshops.security.jwt.JwtUtils;
import com.example.dreamshops.security.user.ShopUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            ShopUserDetails shopUserDetails = (ShopUserDetails) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(shopUserDetails.getId(), jwt);
            return ResponseEntity.ok(new ApiResponse("Login success", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(UNAUTHORIZED)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
