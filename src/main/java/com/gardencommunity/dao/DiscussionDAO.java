package com.gardencommunity.dao;

import com.gardencommunity.model.Discussion;

import java.util.List;

public interface DiscussionDAO extends DAO<Discussion> {
    List<Discussion> findAll() throws DataAccessException;  // override to ensure ordering if you want
}
