package com.example.notesapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notesapp.Model.Notes;
import com.example.notesapp.Repository.NotesRepository;

import java.util.List;
/**
 * @author Vineet
 * Date: 27th January, 2022
 * NotesViewModel: MainActivity will interact with NotesViewModel for all ui change operations.
 */
public class NotesViewModel extends AndroidViewModel {
    public NotesRepository repository;
    public LiveData<List<Notes>> getAllNotes;

    public NotesViewModel(Application application) {
        super(application);
        repository = new NotesRepository(application);
        getAllNotes=repository.getAllNotes;
    }
   public void insertNote(Notes notes){
        repository.insertNotes(notes);
    }

   public void deleteNote(int id){
        repository.deleteNotes(id);
    }
   public void updateNote(Notes notes){
        repository.updateNotes(notes);
    }
}
