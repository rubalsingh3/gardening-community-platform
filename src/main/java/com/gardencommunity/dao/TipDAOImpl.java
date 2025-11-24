package com.gardencommunity.dao;

import com.gardencommunity.db.DatabaseManager;
import com.gardencommunity.model.Tip;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TipDAOImpl implements TipDAO {

    private Tip mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int authorId = rs.getInt("author_id");
        String title = rs.getString("title");
        String description = rs.getString("description");
        Tip.Status status = Tip.Status.valueOf(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime created = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();
        return new Tip(id, authorId, title, description, status, created);
    }

    @Override
    public Tip findById(int id) throws DataAccessException {
        String sql = "SELECT * FROM tips WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findById tip failed", e);
        }
    }

    @Override
    public List<Tip> findAll() throws DataAccessException {
        String sql = "SELECT * FROM tips ORDER BY created_at DESC";
        List<Tip> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("findAll tips failed", e);
        }
    }

    @Override
    public Tip save(Tip tip) throws DataAccessException {
        // insert only for now (id == 0)
        if (tip.getId() == 0) {
            String sql = """
                INSERT INTO tips(author_id,title,description,status)
                VALUES (?,?,?,?)
                """;
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, tip.getAuthorId());
                ps.setString(2, tip.getTitle());
                ps.setString(3, tip.getDescription());
                ps.setString(4, tip.getStatus().name());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        return new Tip(id, tip.getAuthorId(), tip.getTitle(),
                                tip.getDescription(), tip.getStatus(), LocalDateTime.now());
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException("insert tip failed", e);
            }
            return null;
        } else {
            // update status/title/description
            String sql = "UPDATE tips SET title=?, description=?, status=? WHERE id=?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tip.getTitle());
                ps.setString(2, tip.getDescription());
                ps.setString(3, tip.getStatus().name());
                ps.setInt(4, tip.getId());
                ps.executeUpdate();
                return tip;
            } catch (SQLException e) {
                throw new DataAccessException("update tip failed", e);
            }
        }
    }

    @Override
    public boolean delete(int id) throws DataAccessException {
        String sql = "DELETE FROM tips WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("delete tip failed", e);
        }
    }

    @Override
    public List<Tip> findByAuthor(int authorId) throws DataAccessException {
        String sql = "SELECT * FROM tips WHERE author_id = ? ORDER BY created_at DESC";
        List<Tip> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("findByAuthor failed", e);
        }
    }

    @Override
    public List<Tip> findPending() throws DataAccessException {
        String sql = "SELECT * FROM tips WHERE status='PENDING' ORDER BY created_at DESC";
        List<Tip> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("findPending failed", e);
        }
    }
}
