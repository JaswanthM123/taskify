package com.shristi.taskify.service.impl;

import com.shristi.taskify.configuration.JwtTokenProvider;
import com.shristi.taskify.dto.*;
import com.shristi.taskify.exception.UserExistException;
import com.shristi.taskify.model.Role;
import com.shristi.taskify.model.User;
import com.shristi.taskify.repository.IAuthRepository;
import com.shristi.taskify.service.declaration.IAuthService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.shristi.taskify.model.Role.ADMIN;

@Service
public class AuthServiceImpl implements IAuthService {

    private final IAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(IAuthRepository authRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponse registerUser(AuthRequest authRequest) {
        if (authRepository.findByEmail(authRequest.getEmail()).isPresent()) {
            throw new UserExistException("User already exists: " + authRequest.getUserName());
        }
        User user=new User();
        user.setUserName(authRequest.getUserName());
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRole(Role.USER);
        User registerUser = authRepository.save(user);
        return new AuthResponse(registerUser.getUserId(), registerUser.getUserName());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = authRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(),user.getRole().name());
        return new LoginResponse(token, user.getUserName(), user.getRole().name());
    }

   // Special API for ADMIN registering
   @Override
   public AuthResponse registerAdmin(AuthRequest authRequest) {
        if (authRepository.findByEmail(authRequest.getEmail()).isPresent()) {
            throw new UserExistException("Admin already exists");
        }

        User admin = User.builder()
                .userName(authRequest.getUserName())
                .email(authRequest.getEmail())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .role(Role.ADMIN)
                .build();

        User savedAdmin = authRepository.save(admin);
       return new AuthResponse(savedAdmin.getUserId(),savedAdmin.getUserName());
    }
}
