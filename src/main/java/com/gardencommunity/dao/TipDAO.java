package com.gardencommunity.dao;

import com.gardencommunity.model.Tip;

import java.util.List;

public interface TipDAO extends DAO<Tip> {
    List<Tip> findByAuthor(int authorId) throws DataAccessException;
    List<Tip> findPending() throws DataAccessException;
}
