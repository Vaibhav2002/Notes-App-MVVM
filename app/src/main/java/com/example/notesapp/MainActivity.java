package com.example.notesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.databinding.ActivityMainBinding;
import com.example.notesapp.room.Note;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    //intent codes
    public static final String EXTRA_TITLE = "Title";
    public static final String EXTRA_DESCRIPTION = "Descr";
    public static final String EXTRA_ID = "EXTRA_ID";
    //intent codes
    //request codes
    private static final int ADD_NOTE_CODE = 1;
    private static final int EDIT_NOTE_CODE = 2;
    private static final int DELETE_CODE = 3;
    private static final int DELETE_ALL_CODE = 4;
    //request codes

    NoteViewModel noteViewModel;
    NoteAdapter noteAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delAll)
            showDialog(getString(R.string.DeleteAllTitle), getString(R.string.DeleteAllMessage), DELETE_ALL_CODE, null);
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_CODE && resultCode == AddEditNote.RESULT_OK) {
            assert data != null;
            String title = Objects.requireNonNull(data.getStringExtra(EXTRA_TITLE)).trim();
            String description = Objects.requireNonNull(data.getStringExtra(EXTRA_DESCRIPTION)).trim();
            noteViewModel.insert(new Note(title, description));
            Toast.makeText(this, "Toast saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_CODE && resultCode == AddEditNote.RESULT_OK) {
            String title = Objects.requireNonNull(Objects.requireNonNull(data).getStringExtra(EXTRA_TITLE)).trim();
            String description = Objects.requireNonNull(data.getStringExtra(EXTRA_DESCRIPTION)).trim();
            int id = data.getIntExtra(EXTRA_ID, -1);
            Note n = new Note(title, description);
            n.setId(id);
            noteViewModel.update(n);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cannot save note", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setLifecycleOwner(this);
        noteAdapter = new NoteAdapter(this);
        activityMainBinding.recycle.setHasFixedSize(true);
        activityMainBinding.recycle.setAdapter(noteAdapter);
        noteViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteAdapter.setList(notes);
            }
        });
        activityMainBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNote.class);
                startActivityForResult(intent, ADD_NOTE_CODE);
            }
        });


        noteAdapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNote.class);
                intent.putExtra(EXTRA_TITLE, note.getTitle());
                intent.putExtra(EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(EXTRA_ID, note.getId());
                startActivityForResult(intent, EDIT_NOTE_CODE);
            }
        });
        noteAdapter.setOnItemLongClickListener(new NoteAdapter.onItemLongCLickListener() {
            @Override
            public void onItemLongClick(Note note) {
                showDialog(getString(R.string.DeleteTitle), getString(R.string.DeleteMessage), DELETE_CODE, note);
            }
        });

    }


    void showDialog(String title, String Message, final int request, final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(Message)
                .setCancelable(true)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (request == DELETE_ALL_CODE)
                            deleteAll();
                        else
                            delete(note);
                    }
                })
                .setNegativeButton("Cancel", null);
        AlertDialog al = builder.create();
        al.show();
    }

    void deleteAll() {
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

    void delete(final Note note) {
        noteViewModel.delete(note);
        Snackbar snackbar = Snackbar.make(findViewById(R.id.rellayout), "Undo", Snackbar.LENGTH_SHORT)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noteViewModel.insert(note);
                    }
                });
        snackbar.show();
    }

}