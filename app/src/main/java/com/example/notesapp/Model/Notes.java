package com.example.notesapp.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notes_Table")
public class Notes {
   @PrimaryKey(autoGenerate = true)
    public int id;

   @ColumnInfo(name = "notes_title")
    public String notesTitle;

   @ColumnInfo(name = "notes")
    public String notes;

   @ColumnInfo(name="media")
    public String image;

   @ColumnInfo(name = "video")
    public String video;


}
