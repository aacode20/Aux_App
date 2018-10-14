package com.example.spenc.aux_20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void JoinActivity(View view) {
        Intent intent = new Intent(this, Join.class);
        startActivity(intent);
        finish();
    }

    public void HostActivity(View view) {
        Intent intent = new Intent(this, Host.class);
        startActivity(intent);
        finish();
    }
//Maybe handle send text here so that it reroutes

}
