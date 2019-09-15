package com.yut.originalqualityphotoshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class clientReceivingActivity extends AppCompatActivity {

    Socket sock;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_recieving);
        textView= findViewById(R.id.test);
        String detected=getDetected();
        String[] ipAndport= parseDetected(detected);
        textView.setText("First: "+ipAndport[0]+"\n Second: "+ipAndport[1]);


        try {
            this.sock = new Socket(ipAndport[0], Integer.parseInt(ipAndport[1]));
            textView.setText("connected");
        }
        catch (IOException e){
            textView.setText("IO exception");
        }

    }



    public String getDetected(){
        Bundle extras= getIntent().getExtras();
        String detected=extras.getString("detected");
        return detected;
    }


    public String[] parseDetected(String detected){
        String[] array;
        String[] array2;

        array= detected.split("/");
        array2= array[1].split(":");
        return array2;
        }


}


