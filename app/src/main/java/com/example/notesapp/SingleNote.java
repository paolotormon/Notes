package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import static com.example.notesapp.MainActivity.arrayAdapter;
import static com.example.notesapp.MainActivity.notesList;
import static com.example.notesapp.MainActivity.sharedPreferences;

public class SingleNote extends AppCompatActivity {

    EditText noteEditText;
    String savenote="";
    int position;
    boolean newNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note);
        noteEditText=findViewById(R.id.noteEditText);
        Intent intent = getIntent();
        newNote = intent.getBooleanExtra("isNew", false);
        if (!newNote) {
            position = intent.getIntExtra("position",0);
            noteEditText.setText(notesList.get(position));
        }
    }

    @Override
    protected void onPause() {
        savenote = noteEditText.getText().toString();
        if(newNote){
            notesList.add(savenote);
        }else{
            notesList.remove(position);
            notesList.add(position,savenote);
        }
        try {
            sharedPreferences.edit().putString("notesList",ObjectSerializer.serialize(notesList)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        arrayAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        super.onPause();
    }


}
