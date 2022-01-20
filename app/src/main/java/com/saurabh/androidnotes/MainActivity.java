package com.saurabh.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    RecyclerView recyclerView;
    ActivityAdapter adapter;
    List<Notes> notesList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);
        adapter=new ActivityAdapter(notesList,this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

    }


    public ArrayList<Notes> loadFile(){

        ArrayList<Notes> list = new ArrayList<>();
        try {
            InputStream inputStream = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }
            JSONArray jsonArray = new JSONArray(stringBuffer.toString());
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String detail = jsonObject.getString("detail");
                String date = jsonObject.getString("date");
                Notes notes = new Notes(title,detail,date);
                list.add(notes);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onStart() {
        notesList.clear();
        notesList.addAll(loadFile());
        setTitle("Android Notes ("+notesList.size()+")");
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(this,EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("List",(Serializable) notesList);
                intent.putExtra("List",bundle);
                startActivity(intent);
                return true;
            case R.id.info:
                Intent info = new Intent(this,AboutActivity.class);
                startActivity(info);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNotes(){
        try {
            FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.print(notesList);
            printWriter.close();
            fileOutputStream.close();
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        Notes notes = notesList.get(position);
        Intent intent = new Intent(this,EditActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("title",notes.getTitle());
        intent.putExtra("detail",notes.getDetail());
        bundle.putSerializable("List",(Serializable) notesList);
        intent.putExtra("List",bundle);
        intent.putExtra("position",position);
        startActivity(intent);

      }

    @Override
    public boolean onLongClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        new AlertDialog.Builder(this,R.style.MyDialogTheme)
                .setTitle("Delete Note '"+notesList.get(position).getTitle()+"'?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notesList.remove(position);
                        saveNotes();
                        setTitle("Android Notes ("+notesList.size()+")");
                        adapter.notifyDataSetChanged();

                    }
                })
                .setNegativeButton("No",null)
                .show();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
    }
}