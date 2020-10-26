package com.example.notesapp.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Note.class,version =1)
public abstract class noteDatabase extends RoomDatabase {

    private static noteDatabase instance;

    public abstract noteDao getDao();

    public static synchronized noteDatabase getInstance(Context context)
    {
        if(instance==null)
        {
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    noteDatabase.class,"notesDatabase")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    static RoomDatabase.Callback callback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateDb(instance).execute();
        }
    };

    static class populateDb extends AsyncTask<Void,Void,Void>{

        com.example.notesapp.room.noteDao noteDao;
        populateDb(noteDatabase database)
        {
            noteDao=database.getDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Hello","World"));
            return null;
        }
    }


}
