package com.gardencommunity.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    // H2 DB file in your user home
    private static final String JDBC_URL  = "jdbc:h2:~/garden_community_db;AUTO_SERVER=TRUE";
    private static final String USER      = "sa";
    private static final String PASSWORD  = "";

    static {
        try {
            // 1) make sure H2 driver is loaded
            Class.forName("org.h2.Driver");
            // 2) create tables + seed admin
            initSchema();
        } catch (Exception e) {
            e.printStackTrace(); // don't rethrow, or Tomcat will die
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    private static void initSchema() throws SQLException {
        try (Connection conn = getConnection()) {

            // create users table
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id IDENTITY PRIMARY KEY,
                        name  VARCHAR(100) NOT NULL,
                        email VARCHAR(150) NOT NULL UNIQUE,
                        role  VARCHAR(20)  NOT NULL
                    )
                    """);
            }

            // create tips table
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("""
        CREATE TABLE IF NOT EXISTS tips (
            id IDENTITY PRIMARY KEY,
            author_id INT NOT NULL,
            title       VARCHAR(255) NOT NULL,
            description VARCHAR(2000),
            status      VARCHAR(20) NOT NULL,
            created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_tip_user
                FOREIGN KEY (author_id) REFERENCES users(id)
        )
        """);
            }

            // create discussions table
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("""
        CREATE TABLE IF NOT EXISTS discussions (
            id IDENTITY PRIMARY KEY,
            author_id INT NOT NULL,
            title      VARCHAR(255) NOT NULL,
            content    VARCHAR(4000),
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_disc_user
                FOREIGN KEY (author_id) REFERENCES users(id)
        )
        """);
            }

            // create projects table
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("""
        CREATE TABLE IF NOT EXISTS projects (
            id IDENTITY PRIMARY KEY,
            owner_id   INT NOT NULL,
            title      VARCHAR(255) NOT NULL,
            description VARCHAR(2000),
            progress   INT NOT NULL DEFAULT 0,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_proj_user
                FOREIGN KEY (owner_id) REFERENCES users(id)
        )
        """);
            }


            // seed admin if not exists
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement ins = conn.prepareStatement(
                            "INSERT INTO users(name,email,role) VALUES (?,?,?)")) {
                        ins.setString(1, "Admin");
                        ins.setString(2, "admin@garden.com");
                        ins.setString(3, "ADMIN");
                        ins.executeUpdate();
                    }
                }
            }
        }
    }
}
