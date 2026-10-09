package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.exception.DatabaseException;
import com.lingesh.lingeshmart.model.User;
import com.lingesh.lingeshmart.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole().name());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }
            return user;
        } catch (SQLException e) {
            throw new DatabaseException("Error creating user: " + user.getEmail(), e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, name, email, password_hash, role, created_at FROM users WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding user by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, name, email, password_hash, role, created_at FROM users WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding user by email: " + email, e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT id, name, email, password_hash, role, created_at FROM users ORDER BY id ASC";
        List<User> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all users", e);
        }
        return list;
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getRole().name());
            ps.setLong(4, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating user ID: " + user.getId(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user ID: " + id, e);
        }
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(User.Role.valueOf(rs.getString("role")));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }
}
