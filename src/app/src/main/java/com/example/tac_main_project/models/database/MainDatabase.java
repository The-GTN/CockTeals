package com.example.tac_main_project.models.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tac_main_project.models.dao.MainDao;
import com.example.tac_main_project.models.entities.MainEntity;

@Database(entities = {MainEntity.class}, version = 1)
public abstract class MainDatabase extends RoomDatabase {
    public abstract MainDao CocktailDao();
}

