package com.example.spenc.aux_20;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QueueService extends IntentService {

    private DatabaseReference database;
    private ValueEventListener listener;
    private String hostID;


    public QueueService(String in){
        super(in);
    }

    private void addListener(String hostID){
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String song = (String) dataSnapshot.getValue();
                if(song != null && !song.equals("")){
                    Host.processSong(song);
                    database.child(dataSnapshot.getKey()).setValue("");
                }

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

    private DatabaseReference newHost(String hostID){
        DatabaseReference dbr = database.child(hostID);
        dbr.setValue("");
        return dbr;
    }

    private void removeHost(String hostID){
        DatabaseReference dbr = database.child(hostID);
        dbr.removeValue();
    }

    @Override
    public int onStartCommand(Intent i, int a, int b){
        //if(work.getBooleanExtra("Start",true)){
        initDB();
        connectDB();
        //hostID = work.getStringExtra("Token");
        hostID = Host.hostID;
        newHost(hostID);
        addListener(hostID);
        Log.d("host", hostID);
        /*}else{
            removeHost(hostID);
            disconnectDB();
            removeListener(hostID);
        }*/
        return Service.START_NOT_STICKY;
    }

    @Override
    protected void onCreate(){

    }

    @Override
    protected void onHandleIntent(Intent work){
        //if(work.getBooleanExtra("Start",true)){
            initDB();
            connectDB();
            //hostID = work.getStringExtra("Token");
            hostID = Host.hostID;
            newHost(hostID);
            addListener(hostID);
            Log.d("host", hostID);
        /*}else{
            removeHost(hostID);
            disconnectDB();
            removeListener(hostID);
        }*/
    }




}