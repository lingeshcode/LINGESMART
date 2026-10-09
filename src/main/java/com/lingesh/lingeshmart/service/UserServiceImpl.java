package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.dao.UserDAO;
import com.lingesh.lingeshmart.dto.UserResponseDTO;
import com.lingesh.lingeshmart.exception.AuthenticationException;
import com.lingesh.lingeshmart.exception.ResourceNotFoundException;
import com.lingesh.lingeshmart.exception.ValidationException;
import com.lingesh.lingeshmart.model.User;
import com.lingesh.lingeshmart.util.PasswordUtil;
import com.lingesh.lingeshmart.util.ValidationUtil;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User registerUser(String name, String email, String password, User.Role role) {
        ValidationUtil.validateNotEmpty("Name", name);
        ValidationUtil.validateEmail(email);
        ValidationUtil.validateNotEmpty("Password", password);

        if (password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters long");
        }

        if (userDAO.findByEmail(email.toLowerCase().trim()).isPresent()) {
            throw new ValidationException("Email is already registered. Please login.");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.toLowerCase().trim());
        user.setPasswordHash(hashedPassword);
        user.setRole(role != null ? role : User.Role.BUYER);

        return userDAO.create(user);
    }

    @Override
    public User loginUser(String email, String password) {
        ValidationUtil.validateEmail(email);
        ValidationUtil.validateNotEmpty("Password", password);

        User user = userDAO.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        if (!PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid email or password");
        }

        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userDAO.findAll().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }
}
