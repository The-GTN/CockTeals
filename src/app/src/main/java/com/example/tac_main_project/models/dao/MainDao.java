package com.example.tac_main_project.models.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tac_main_project.models.entities.MainEntity;

import java.util.List;

@Dao
public interface MainDao {
    @Query("SELECT * FROM MainEntity")
    List<MainEntity> getAll();

    @Query("SELECT * FROM MainEntity WHERE cocktailName IN (:cocktailsNames)")
    List<MainEntity> loadAllByNames(String[] cocktailsNames);

    @Insert
    void insertAll(MainEntity... cocktailEntities);

    @Delete
    void delete(MainEntity mainEntity);

    @Query("DELETE FROM MainEntity")
    public void nukeTable();
}
