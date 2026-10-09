package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    User create(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    boolean update(User user);
    boolean delete(Long id);
}
