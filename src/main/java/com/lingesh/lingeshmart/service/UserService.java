package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.dto.UserResponseDTO;
import com.lingesh.lingeshmart.model.User;
import java.util.List;

public interface UserService {
    User registerUser(String name, String email, String password, User.Role role);
    User loginUser(String email, String password);
    User getUserById(Long id);
    List<UserResponseDTO> getAllUsers();
}
