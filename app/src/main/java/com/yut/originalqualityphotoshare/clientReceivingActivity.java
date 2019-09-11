package com.yut.originalqualityphotoshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.net.Socket;

public class clientReceivingActivity extends AppCompatActivity {

    Socket sock;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_recieving);
        textView= findViewById(R.id.test);
        String detected=getDetected();
        textView.setText(detected);

        //String[] ipAndport= FormatString(detected);

        //this.sock= new Socket(ipAddress, port);


    }



    public String getDetected(){
        Bundle extras= getIntent().getExtras();
        String detected=extras.getString("detected");
        return detected;
    }
/*
    public String[] FormatString(String str){
            String[] array= str.split("");
            String[] finalArray;
            for(String s: array){


        }
    }
    */
}


