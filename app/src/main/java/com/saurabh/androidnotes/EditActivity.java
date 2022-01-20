package com.saurabh.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private EditText titleNotes,detailNotes;
    private  ArrayList<Notes> notes;
    String detail,title;
    int i=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        titleNotes=findViewById(R.id.title_notes);
        detailNotes=findViewById(R.id.detail_notes);
        i=-1;

        Bundle bundle = getIntent().getBundleExtra("List");

        notes= (ArrayList<Notes>) bundle.getSerializable("List");
        Bundle bundle2 = getIntent().getExtras();
        title = bundle2.getString("title");
        if(title != null) {
            detail = bundle2.getString("detail");
            i = bundle2.getInt("position");
            titleNotes.setText(title);
            detailNotes.setText(detail);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void saveNotes(){
        try {
            FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.print(notes);
            printWriter.close();
            fileOutputStream.close();
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_list_menu, menu);
        return true;
    }

    private boolean addNotes(){
        if(!titleNotes.getText().toString().isEmpty()){
            String strDate = new SimpleDateFormat("E MMM dd, hh:mm aa ").format(new Date());
            Notes notesInfo = new Notes(titleNotes.getText().toString(),detailNotes.getText().toString(),strDate);
            if(i>=0){
                if (!title.equals(titleNotes.getText().toString()) || !detail.equals(detailNotes.getText().toString())) {
                    notes.remove(i);
                }else {
                    return true;
                }
            }
            notes.add(notesInfo);
            saveNotes();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                Intent intent=new Intent(this,MainActivity.class);
                 if(!addNotes()){
                     Toast.makeText(this,"Please Fill Title",Toast.LENGTH_SHORT).show();
                     AlertDialogForEmpty();
                    return true;
                }

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
    public void AlertDialogForEmpty(){
        Intent intent = new Intent(this,MainActivity.class);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Please Fill Title!")
                .setMessage("The note will not be saved without a title.\n"+"Do you want to continue?\n")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void toast(){
        Toast.makeText(this,"Please Fill Title",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        String t = titleNotes.getText().toString();
        if(i>-1 && title.equals(titleNotes.getText().toString()) && detail.equals(detailNotes.getText().toString())){
            addNotes();
            startActivity(intent);
        }else if(t.isEmpty() && detailNotes.getText().toString().isEmpty()){
            startActivity(intent);
        }else{
            new AlertDialog.Builder(this,R.style.MyDialogTheme)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Save Note '"+t+"'?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(t.isEmpty()){
                                toast();
                            }else {
                                addNotes();
                                startActivity(intent);
                            }
                        }

                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(intent);
                        }
                    })
                    .show();
        }
    }
}