package com.example.tac_main_project.models.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MainEntity {

    @PrimaryKey
    @NonNull
    public String cocktailName;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "theme")
    public String theme;

    @ColumnInfo(name = "ingredients")
    public String ingredients;

    @ColumnInfo(name = "measures")
    public String measures;

    @ColumnInfo(name = "viewUrl")
    public String viewUrl;

    @ColumnInfo(name = "viewBytesArray")
    public byte[] viewBytesArray;

    @ColumnInfo(name = "i")
    public String i;
}
