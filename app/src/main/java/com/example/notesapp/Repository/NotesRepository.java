package com.example.notesapp.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.notesapp.Dao.NotesDao;
import com.example.notesapp.Database.NotesDatabase;
import com.example.notesapp.Model.Notes;

import java.util.List;

public class NotesRepository {
    public NotesDao notesDao;
    public LiveData<List<Notes>> getAllNotes;

    public NotesRepository(Application application){
        NotesDatabase notesDatabase = NotesDatabase.getDatabaseInstance(application);
        notesDao=notesDatabase.notesDao();
        getAllNotes = notesDao.getAllNotes();

    }
    public void insertNotes(Notes notes){
        notesDao.insertNotes(notes);
    }
   public void deleteNotes(int id){
        notesDao.deleteNotes(id);
    }
    public void updateNotes(Notes notes){
        notesDao.updateNotes(notes);
    }
}

