package com.example.spenc.aux_20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.String;

public class Join extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_screen);
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
        //Send songName to database here
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
