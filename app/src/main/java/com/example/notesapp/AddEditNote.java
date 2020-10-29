package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddEditNote extends AppCompatActivity {
    TextInputEditText title, description;
    FloatingActionButton fab;
    public static final int RESULT_OK = 1;
    int editAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);
        //initialization
        title = findViewById(R.id.titleinput);
        description = findViewById(R.id.descriptioninput);
        fab = findViewById(R.id.saveFab);
        //initialization
        editAdd = getIntent().getIntExtra(MainActivity.EXTRA_ID, -1);
        if (editAdd == -1)
            setTitle("Add new note");
        else {
            setTitle("Edit note");
            title.setText(getIntent().getStringExtra(MainActivity.EXTRA_TITLE));
            description.setText(getIntent().getStringExtra(MainActivity.EXTRA_DESCRIPTION));
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });


    }

    private boolean validate(String title, String description) {
        boolean flag = true;
        if (title.trim().isEmpty()) {
            this.title.setError("Field cannot be empty");
            flag = false;
        }
        if (description.trim().isEmpty()) {
            this.description.setError("Field cannot be empty");
            flag = false;
        }
        return flag;
    }

    private void saveNote() {
        String titletext = title.getText().toString().trim();
        String descriptiontext = description.getText().toString().trim();
        if (validate(titletext, descriptiontext)) {
            Intent intent = new Intent(AddEditNote.this, MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_TITLE, titletext);
            intent.putExtra(MainActivity.EXTRA_DESCRIPTION, descriptiontext);
            if (editAdd != -1)
                intent.putExtra(MainActivity.EXTRA_ID, editAdd);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}