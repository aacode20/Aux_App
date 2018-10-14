package com.example.spenc.aux_20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String hostID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void JoinActivity(View view) {
        EditText content = (EditText) findViewById(R.id.editText);
        String str = content.getText().toString();
        if(str.length() != 5){
            Toast.makeText(getApplicationContext(), "Please enter a valid 5 character party code!", Toast.LENGTH_LONG).show();
        }
        else {
            hostID = str;
            Intent intent = new Intent(this, Join.class);
            intent.putExtra("token", str);
            startActivity(intent);
            finish();
        }
    }

    public void HostActivity(View view) {
        Intent intent = new Intent(this, Host.class);
        startActivity(intent);
        finish();
    }
}
