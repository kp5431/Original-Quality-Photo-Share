package com.yut.originalqualityphotoshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class serverSendingActivity extends AppCompatActivity {

    String[] files;
    TextView serverText;

    serverSending server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_sending);
        this.files = getIntent().getStringArrayExtra("FILE_STRINGS");
        this.serverText = findViewById(R.id.serverText);
        new serverSending().execute(files);
        


    }
}
