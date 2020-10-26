package com.example.notesapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notesapp.room.Note;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    noteRepository noteRepository;
    LiveData<List<Note>> list;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository=new noteRepository(application);
        list=noteRepository.getAllNotes();
    }
    void insert(Note note)
    {
        noteRepository.insert(note);
    }
    void update(Note note)
    {
        noteRepository.update(note);
    }
    void delete(Note note)
    {
        noteRepository.delete(note);
    }
    void deleteAll()
    {
        noteRepository.deleteAll();
    }
    LiveData<List<Note>> getAllNotes()
    {
        return list;
    }

}
