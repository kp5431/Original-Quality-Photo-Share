package com.yut.originalqualityphotoshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class serverSendingActivity extends AppCompatActivity {

    ServerSocket serverSocket;
    String[] files;
    TextView serverText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_sending);
        this.files=getIntent().getStringArrayExtra("FILE_STRINGS");
        this.serverText= findViewById(R.id.serverText);

        try {
            serverSocket = new ServerSocket(6969);
            serverText.setText("server socket created");
        }
        catch(IOException io){
            serverText.setText("server socket failed");

            io.printStackTrace();
        }
        while(!serverSocket.isClosed()){       //may be incorrect boolean, should be while server is running
            try {
                Socket clientSocket= serverSocket.accept();
                serverText.setText("client connection established");
                ClientThread client= new ClientThread(files, clientSocket);
                Thread thread= new Thread(client);
                thread.start();
                serverText.setText("clientThread started");


            }
            catch (IOException io){
                serverText.setText("failed to create client socket");

                io.printStackTrace();
            }
        }
    }
}
