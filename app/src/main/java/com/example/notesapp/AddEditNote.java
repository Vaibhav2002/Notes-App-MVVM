package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.notesapp.databinding.ActivityAddEditNoteBinding;

public class AddEditNote extends AppCompatActivity {
    ActivityAddEditNoteBinding activityAddEditNoteBinding;
    public static final int RESULT_OK = 1;
    int editAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddEditNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_note);
        editAdd = getIntent().getIntExtra(MainActivity.EXTRA_ID, -1);
        if (editAdd == -1)
            setTitle("Add new note");
        else {
            setTitle("Edit note");
            activityAddEditNoteBinding.titleinput.setText(getIntent().getStringExtra(MainActivity.EXTRA_TITLE));
            activityAddEditNoteBinding.descriptioninput.setText(getIntent().getStringExtra(MainActivity.EXTRA_DESCRIPTION));
        }


        activityAddEditNoteBinding.saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });


    }

    private boolean validate(String title, String description) {
        boolean flag = true;
        if (title.trim().isEmpty()) {
            this.activityAddEditNoteBinding.titleinput.setError("Field cannot be empty");
            flag = false;
        }
        if (description.trim().isEmpty()) {
            this.activityAddEditNoteBinding.descriptioninput.setError("Field cannot be empty");
            flag = false;
        }
        return flag;
    }

    private void saveNote() {
        String titletext = activityAddEditNoteBinding.titleinput.getText().toString().trim();
        String descriptiontext = activityAddEditNoteBinding.descriptioninput.getText().toString().trim();
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