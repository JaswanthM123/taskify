package com.shristi.taskify.service.declaration;

import com.shristi.taskify.dto.UserResponse;

import java.security.Principal;
import java.util.List;

public interface IUserService {
    UserResponse getCurrentUser(Principal principal);
    List<UserResponse> getAllUsers();
}
