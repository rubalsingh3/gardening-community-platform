package com.gardencommunity.dao;

import com.gardencommunity.model.User;

public interface UserDAO extends DAO<User> {
    User findByEmail(String email) throws DataAccessException;
}
