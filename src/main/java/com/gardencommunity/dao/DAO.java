package com.gardencommunity.dao;

import java.util.List;

public interface DAO<T> {
    T findById(int id) throws DataAccessException;
    List<T> findAll() throws DataAccessException;
    T save(T entity) throws DataAccessException;   // insert or update
    boolean delete(int id) throws DataAccessException;
}
