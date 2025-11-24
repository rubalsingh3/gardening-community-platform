package com.gardencommunity.dao;

import com.gardencommunity.model.Project;

import java.util.List;

public interface ProjectDAO extends DAO<Project> {
    List<Project> findByOwner(int ownerId) throws DataAccessException;
}
