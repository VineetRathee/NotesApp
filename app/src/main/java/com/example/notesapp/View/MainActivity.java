package com.example.notesapp.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.notesapp.Adapter.NotesAdapter;
import com.example.notesapp.Model.Notes;
import com.example.notesapp.R;
import com.example.notesapp.ViewModel.NotesViewModel;
import com.example.notesapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vineet
 * Date: 1st November, 2021
 * MainActivity it is used to hold the recycler view , update adapter and to search the notes
 */
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    NotesViewModel notesViewModel;
    NotesAdapter notesAdapter;
    List<Notes> searchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(MainActivity.this);
        binding.recycler.setAdapter(notesAdapter);
        binding.newNotes.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddNotes.class)));

        notesViewModel.getAllNotes.observe(this, notes -> {
           notesAdapter.addNotes(notes);
           searchList=notes;
        });

    }
    /**
     * @param menu
     * In this search menu is created and onQueryTextChange is to check the real time insertion of characters in search bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Search Notes..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchNotes(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * @param text
     * Filter the notes based on the string
     */
    private void searchNotes(String text){
        Log.d("search", "searchNotes: "+ text);
        List<Notes> searchList = new ArrayList<>();
        for(Notes note: this.searchList){
            if(note.notesTitle.contains(text)||note.notes.contains(text)){
                searchList.add(note);
            }
        }
        notesAdapter.addNotes(searchList);
    }
}