package com.example.spenc.aux_20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.util.HashMap;

public class Host extends AppCompatActivity {

    private static final String CLIENT_ID = "172bb1fe694d4e21b6391329553a52fa";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private DatabaseReference database;
    private ValueEventListener listener;
    private String hostID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_screen);
        initDB();
        connectDB();
        hostID = generateToken();
        newHost(hostID);
        addListener(hostID);
    }

    private void configDummyDB(){
        HashMap<String,String> HostID =  new HashMap<>();
        HostID.put("12345", "Monkey");
        HostID.put("54321", "Test");
        database.setValue(HostID);
    }

    private DatabaseReference newHost(String hostID){
        DatabaseReference dbr = database.child(hostID);
        dbr.setValue("");
        return dbr;
    }

    private void removeHost(String hostID){
        DatabaseReference dbr = database.child(hostID);
        dbr.removeValue();
    }

    private void addListener(String hostID){
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String song = (String) dataSnapshot.getValue();
                if(song != null && !song.equals("")){
                    mSpotifyAppRemote.getPlayerApi().queue(song);
                }
                database.child(dataSnapshot.getKey()).setValue("");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read value");
            }
        };
        database.child(hostID).addValueEventListener(listener);
    }

    private void removeListener(String hostID){
        if(database != null && listener != null){
            database.child(hostID).removeEventListener(listener);
        }
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
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.CONNECTOR.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.CONNECTOR.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:user:spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState().setEventCallback(new Subscription.EventCallback<PlayerState>() {

            public void onEvent(PlayerState playerState) {
                final Track track = playerState.track;
                if (track != null) {
                    Log.d("MainActivity", track.name + " by " + track.artist.name);
                }
            }
        });
    }

    public String generateToken(){
        //Would be used to generate unique n length alphanumeric string to identify 'parties'
        //May want to consider string length for overlap/too hard to enter/etc..
        /*
        Usage would be:
            while(token not in database)
                token = generateToken(n);
        */
        int token = (int)(Math.random() * 10000) + 10000;
        return Integer.toString(token);
    }

    public void endActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        removeHost(hostID);
        removeListener(hostID);
        disconnectDB();
        //CLOSE CONNECTION TO DATABASE
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}

