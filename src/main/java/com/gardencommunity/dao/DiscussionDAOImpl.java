package com.gardencommunity.dao;

import com.gardencommunity.db.DatabaseManager;
import com.gardencommunity.model.Discussion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DiscussionDAOImpl implements DiscussionDAO {

    private Discussion mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int authorId = rs.getInt("author_id");
        String title = rs.getString("title");
        String content = rs.getString("content");
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime created = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();
        return new Discussion(id, authorId, title, content, created);
    }

    @Override
    public Discussion findById(int id) throws DataAccessException {
        String sql = "SELECT * FROM discussions WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findById discussion failed", e);
        }
    }

    @Override
    public List<Discussion> findAll() throws DataAccessException {
        String sql = "SELECT * FROM discussions ORDER BY created_at DESC";
        List<Discussion> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("findAll discussions failed", e);
        }
    }

    @Override
    public Discussion save(Discussion d) throws DataAccessException {
        if (d.getId() == 0) {
            String sql = "INSERT INTO discussions(author_id,title,content) VALUES (?,?,?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, d.getAuthorId());
                ps.setString(2, d.getTitle());
                ps.setString(3, d.getContent());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        return new Discussion(id, d.getAuthorId(), d.getTitle(),
                                d.getContent(), LocalDateTime.now());
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException("insert discussion failed", e);
            }
            return null;
        } else {
            String sql = "UPDATE discussions SET title=?, content=? WHERE id=?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, d.getTitle());
                ps.setString(2, d.getContent());
                ps.setInt(3, d.getId());
                ps.executeUpdate();
                return d;
            } catch (SQLException e) {
                throw new DataAccessException("update discussion failed", e);
            }
        }
    }

    @Override
    public boolean delete(int id) throws DataAccessException {
        String sql = "DELETE FROM discussions WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("delete discussion failed", e);
        }
    }
}
