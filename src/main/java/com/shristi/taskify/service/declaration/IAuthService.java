package com.shristi.taskify.service.declaration;

import com.shristi.taskify.dto.*;

public interface IAuthService {
    AuthResponse registerUser(AuthRequest authRequest);
    LoginResponse login(LoginRequest loginRequest);
    // Special API for ADMIN registering
    AuthResponse registerAdmin(AuthRequest authRequest);
}
