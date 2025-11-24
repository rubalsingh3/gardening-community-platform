package com.gardencommunity.dao;

import com.gardencommunity.db.DatabaseManager;
import com.gardencommunity.model.Project;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {

    private Project mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int ownerId = rs.getInt("owner_id");
        String title = rs.getString("title");
        String description = rs.getString("description");
        int progress = rs.getInt("progress");
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime created = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();
        return new Project(id, ownerId, title, description, progress, created);
    }

    @Override
    public Project findById(int id) throws DataAccessException {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findById project failed", e);
        }
    }

    @Override
    public List<Project> findAll() throws DataAccessException {
        String sql = "SELECT * FROM projects ORDER BY created_at DESC";
        List<Project> out = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                out.add(mapRow(rs));
            }
            return out;
        } catch (SQLException e) {
            throw new DataAccessException("findAll projects failed", e);
        }
    }

    @Override
    public Project save(Project p) throws DataAccessException {
        if (p.getId() == 0) {
            String sql = """
                INSERT INTO projects(owner_id,title,description,progress)
                VALUES (?,?,?,?)
                """;
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, p.getOwnerId());
                ps.setString(2, p.getTitle());
                ps.setString(3, p.getDescription());
                ps.setInt(4, p.getProgress());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        return new Project(id, p.getOwnerId(), p.getTitle(),
                                p.getDescription(), p.getProgress(), LocalDateTime.now());
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException("insert project failed", e);
            }
            return null;
        } else {
            String sql = "UPDATE projects SET title=?, description=?, progress=? WHERE id=?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getTitle());
                ps.setString(2, p.getDescription());
                ps.setInt(3, p.getProgress());
                ps.setInt(4, p.getId());
                ps.executeUpdate();
                return p;
            } catch (SQLException e) {
                throw new DataAccessException("update project failed", e);
            }
        }
    }

    @Override
    public boolean delete(int id) throws DataAccessException {
        String sql = "DELETE FROM projects WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("delete project failed", e);
        }
    }

    @Override
    public List<Project> findByOwner(int ownerId) throws DataAccessException {
        String sql = "SELECT * FROM projects WHERE owner_id = ? ORDER BY created_at DESC";
        List<Project> out = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
            }
            return out;
        } catch (SQLException e) {
            throw new DataAccessException("findByOwner failed", e);
        }
    }
}
