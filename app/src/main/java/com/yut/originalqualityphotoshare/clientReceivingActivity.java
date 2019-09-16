package com.yut.originalqualityphotoshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class clientReceivingActivity extends AppCompatActivity {


    TextView textView;
    clientReceiving clientReceiving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_recieving);
        textView= findViewById(R.id.test);
        String detected=getDetected();
        String[] ipAndport= parseDetected(detected);
        textView.setText("First: "+ipAndport[0]+"\n Second: "+ipAndport[1]);
        this.clientReceiving=new clientReceiving();
        clientReceiving.execute(ipAndport);


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


