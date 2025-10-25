package com.shristi.taskify.controller;


import com.shristi.taskify.dto.*;
import com.shristi.taskify.service.declaration.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final IAuthService authService;
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.registerUser(authRequest), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/registeradmin")
    public AuthResponse registerAdmin(@RequestBody AuthRequest authRequest) {
        return authService.registerAdmin(authRequest);
    }
}
