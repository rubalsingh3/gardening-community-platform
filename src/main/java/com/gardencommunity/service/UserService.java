package com.gardencommunity.service;

import com.gardencommunity.dao.DataAccessException;
import com.gardencommunity.dao.UserDAO;
import com.gardencommunity.dao.UserDAOImpl;
import com.gardencommunity.model.Admin;
import com.gardencommunity.model.Gardener;
import com.gardencommunity.model.User;

import java.util.List;

public class UserService {

    private final UserDAO userDAO = new UserDAOImpl();

    public UserService() {
        try {
            ensureAdminExists();
        } catch (Exception e) {
            e.printStackTrace(); // do NOT throw new RuntimeException
        }
    }


    private void ensureAdminExists() {
        try {
            User admin = userDAO.findByEmail("admin@garden.com");
            if (admin == null) {
                userDAO.save(new Admin(0, "Admin", "admin@garden.com"));
            }
        } catch (Exception e) {
            System.out.println("Admin check failed: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public User registerGardener(String name, String email) {
        try {
            return userDAO.save(new Gardener(0, name, email));
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to register gardener", e);
        }
    }

    public User findByEmail(String email) {
        try {
            return userDAO.findByEmail(email);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to find user by email", e);
        }
    }

    public List<User> findAll() {
        try {
            return userDAO.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to list users", e);
        }
    }
}
