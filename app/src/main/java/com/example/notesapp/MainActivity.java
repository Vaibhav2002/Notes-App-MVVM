package com.example.notesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.room.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final int ADD_NOTE_CODE = 1;

    NoteViewModel noteViewModel;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    FloatingActionButton fab;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delAll)
            showDialog();
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_CODE && resultCode == AddEditNote.RESULT_OK) {
            String title = data.getStringExtra("Title").trim();
            String description = data.getStringExtra("Descr").trim();
            noteViewModel.insert(new Note(title, description));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialization
        recyclerView = findViewById(R.id.recycle);
        fab = findViewById(R.id.fab);
        //initialization
        noteAdapter = new NoteAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(noteAdapter);
        noteViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteAdapter.setList(notes);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNote.class);
                startActivityForResult(intent, ADD_NOTE_CODE);
            }
        });


    }


    void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete all notes")
                .setCancelable(true)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final List<Note> notes2 = noteViewModel.getAllNotes().getValue();
                        noteViewModel.deleteAll();
                        assert notes2 != null;
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.rellayout), "Undo", Snackbar.LENGTH_SHORT)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        for (Note i : notes2)
                                            noteViewModel.insert(i);
                                    }
                                });
                        snackbar.show();
                    }
                })
                .setNegativeButton("Cancel", null);
        AlertDialog al = builder.create();
        al.show();
    }

}