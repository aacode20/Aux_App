package com.example.spenc.aux_20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.String;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Join extends AppCompatActivity {

    Bundle bundle = getIntent().getExtras();
    private String hostID = bundle.getString("token");
    //Toast.makeText(getApplicationContext(), hostID, Toast.LENGTH_LONG).show();

    private DatabaseReference database;
    //private String hostID;
    private HashMap<String, String> HostID = new HashMap<>();

    private void configDummyDB(){
        HashMap<String,String> HostID =  new HashMap<>();
        HostID.put("12345", "Monkey");
        HostID.put("54321", "Test");
        database.setValue(HostID);
    }

    private void initDB(){
        database = FirebaseDatabase.getInstance().getReference();
    }

    private void connectDB(){
        DatabaseReference.goOnline();
    }

    private void disconnectDB(){
        DatabaseReference.goOffline();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_screen);
        initDB();
        connectDB();
    }

    @Override
    protected void onStart() {
        super.onStart();
    //Should this be in on start or on create?
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
        }
    }

    public void handleSendText(Intent intent){
        String songName = parseSpotifyURI(intent);
        HostID.put("test ID", songName);
        database.child("test ID").setValue(songName);
        //database.setValue(songName);
    }

    public String parseSpotifyURI(Intent intent){
        String uri = intent.getClipData().toString();
        String output = "spotify:track:";
        //Verifying that user is sending a spotify element of type "track" (instead of album, playlist, etc.)
        String [] dataType = uri.split("/", 6);
        String type = dataType[4];
        if(!type.equals("track")){
            Toast.makeText(getApplicationContext(), "What you tried to queue isn't a song! Please try again", Toast.LENGTH_LONG).show();
        }
        //End of type check
        int begin = uri.lastIndexOf("/") + 1;
        int end = uri.length() - 3;
        String sub = uri.substring(begin, end);
        output+=sub;
        //Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
        return output;
    }

    public void endActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        //CLOSE CONNECTION TO DATABASE
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
