package com.gardencommunity.dao;

import com.gardencommunity.db.DatabaseManager;
import com.gardencommunity.model.Admin;
import com.gardencommunity.model.Gardener;
import com.gardencommunity.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private User mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String role = rs.getString("role");

        if ("ADMIN".equalsIgnoreCase(role)) {
            return new Admin(id, name, email);
        } else {
            return new Gardener(id, name, email);
        }
    }

    @Override
    public User findById(int id) throws DataAccessException {
        String sql = "SELECT id,name,email,role FROM users WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findById failed", e);
        }
    }

    @Override
    public List<User> findAll() throws DataAccessException {
        String sql = "SELECT id,name,email,role FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                users.add(mapRow(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new DataAccessException("findAll failed", e);
        }
    }

    @Override
    public User save(User entity) throws DataAccessException {
        // if id == 0 -> insert, else update
        if (entity.getId() == 0) {
            String sql = "INSERT INTO users(name,email,role) VALUES (?,?,?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, entity.getName());
                ps.setString(2, entity.getEmail());
                ps.setString(3, entity.getRole().name());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        // return new instance with generated id
                        if (entity.getRole() == User.Role.ADMIN) {
                            return new Admin(id, entity.getName(), entity.getEmail());
                        } else {
                            return new Gardener(id, entity.getName(), entity.getEmail());
                        }
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException("insert user failed", e);
            }
        } else {
            String sql = "UPDATE users SET name=?, email=?, role=? WHERE id=?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, entity.getName());
                ps.setString(2, entity.getEmail());
                ps.setString(3, entity.getRole().name());
                ps.setInt(4, entity.getId());
                ps.executeUpdate();
                return entity;
            } catch (SQLException e) {
                throw new DataAccessException("update user failed", e);
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id) throws DataAccessException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("delete user failed", e);
        }
    }

    @Override
    public User findByEmail(String email) throws DataAccessException {
        String sql = "SELECT id,name,email,role FROM users WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findByEmail failed", e);
        }
    }
}
