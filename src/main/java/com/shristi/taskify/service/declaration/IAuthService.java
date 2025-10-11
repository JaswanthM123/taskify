package com.shristi.taskify.service.declaration;

import com.shristi.taskify.dto.AuthRequest;
import com.shristi.taskify.dto.AuthResponse;
import com.shristi.taskify.dto.LoginRequest;
import com.shristi.taskify.dto.LoginResponse;

public interface IAuthService {
    AuthResponse registerUser(AuthRequest authRequest);
    LoginResponse login(LoginRequest loginRequest);
}
