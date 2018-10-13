package com.example.spenc.aux_20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.Result;
import java.util.concurrent.TimeUnit;
import java.lang.String;
import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "172bb1fe694d4e21b6391329553a52fa";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        //super.onStart();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if(Intent.ACTION_SEND.equals(action) && type != null){
            if("text/plain".equals(type)){
                handleSendText(intent);
            }
        }
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

    public void handleSendText(Intent intent){
        String songName = parseSpotifyURI(intent);
        //TODO: Check if songName is empty string, if true then do something else
        //Send songName to database here
    }

    public String parseSpotifyURI(Intent intent){

        String uri;

        //Checking for any exception when gathering data from intent, if
        try {
            uri = intent.getClipData().toString();
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            return "";
        }
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

    public String generateToken(int length){
        //Would be used to generate unique n length alphanumeric string to identify 'parties'
        //May want to consider string length for overlap/too hard to enter/etc..
        /*
        Usage would be:
            while(token not in database)
                token = generateToken(n);
        */
        StringBuilder stringBuilder = new StringBuilder(length);
        for( int i = 0; i < length; i++ )
            stringBuilder.append( stringBuilder.charAt( rnd.nextInt(stringBuilder.length()) ) );
        return (stringBuilder.toString());

    }
}
