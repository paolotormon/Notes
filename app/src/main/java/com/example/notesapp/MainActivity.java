package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notesList;
    static ArrayAdapter<String> arrayAdapter;
    ListView listView;
    static SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.addNote) {
            Log.i("select", "adding note");
            Intent intent = new Intent(getApplicationContext(), SingleNote.class);
            intent.putExtra("isNew", true);
            startActivity(intent);
            return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        sharedPreferences = this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);


        getSavedNotes();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        listView.setAdapter(arrayAdapter);
        onNoteClick();//view and modify
        onNoteLongClick();//delete

    }

    @SuppressWarnings("unchecked")
    public void getSavedNotes() {
        try {
            notesList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString
                            ("notesList", ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onNoteClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SingleNote.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    public void onNoteLongClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int finalPosition = position;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note")
                        .setMessage("Do you want to PERMANENTLY delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notesList.remove(finalPosition);
                                try {
                                    sharedPreferences.edit().putString("notesList", ObjectSerializer.serialize(notesList)).apply();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arrayAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Note Deleted!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


                return true;
            }
        });
    }

}
