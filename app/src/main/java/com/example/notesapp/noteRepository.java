package com.example.notesapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.notesapp.room.Note;
import com.example.notesapp.room.noteDao;
import com.example.notesapp.room.noteDatabase;

import java.util.List;

public class noteRepository {

    private noteDao noteDao;
    LiveData<List<Note>> list;
    noteRepository(Application application)
    {
        noteDatabase notebase= noteDatabase.getInstance(application);
        noteDao=notebase.getDao();
        list=noteDao.getAllNotes();
    }
    void insert(Note note)
    {
        new insertAsync(noteDao).execute(note);
    }
    void update(Note note)
    {
        new updateAsync(noteDao).execute(note);
    }
    void delete(Note note)
    {
        new deleteAsync(noteDao).execute(note);
    }
    void deleteAll()
    {
        new deleteAllAsync(noteDao).execute();
    }
    LiveData<List<Note>> getAllNotes()
    {
        return list;
    }
    static class insertAsync extends AsyncTask<Note,Void,Void>
    {
        noteDao noteDao;
        insertAsync(noteDao n){
            noteDao=n;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }
    static class deleteAllAsync extends AsyncTask<Void,Void,Void>
    {
        noteDao noteDao;
        deleteAllAsync(com.example.notesapp.room.noteDao n){
            noteDao=n;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAll();
            return null;
        }
    }
    static class updateAsync extends AsyncTask<Note,Void,Void>
    {
        noteDao noteDao;
        updateAsync(noteDao n){
            noteDao=n;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }
    static class deleteAsync extends AsyncTask<Note,Void,Void>
    {
        noteDao noteDao;
        deleteAsync(noteDao n){
            noteDao=n;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }


}
