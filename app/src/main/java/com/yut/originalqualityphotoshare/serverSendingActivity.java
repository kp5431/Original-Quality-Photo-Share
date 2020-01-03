package com.yut.originalqualityphotoshare;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;



public class serverSendingActivity extends AppCompatActivity {

    String[] files;
    TextView serverText;

    serverSending server;
    String ipaddress;
    String port;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_sending);
        this.files = getIntent().getStringArrayExtra("FILE_STRINGS");
        this.serverText = findViewById(R.id.serverText);
        ipaddress=getIpAddress();
        this.port="4567";
        this.imageView= findViewById(R.id.qrCodeImageView);

        serverText.setText("Ipaddress: "+ ipaddress +"Port: "+ port);

        MultiFormatWriter multiFormatWriter= new MultiFormatWriter();
        try{
            BitMatrix bitMatrix= multiFormatWriter.encode(ipaddress+":"+port, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder= new BarcodeEncoder();
            Bitmap bitmap= barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);

        }
        catch(WriterException e){
            System.out.println("writerException");
        }

        this.server= new serverSending();
        server.execute(files);
    }

    public String getIpAddress(){

        NetworkThread NetworkThread= new NetworkThread();
        NetworkThread.start();
        while(true){
            if(NetworkThread.getIpaddress()==null){
                continue;
            }
            else {
                return NetworkThread.getIpaddress();
            }
        }
    }
/*
    public String[] formatFilenames(String[] incoming){
        String[] outgoing;
        for(int i=0; i<files.length; i++){

        }
        return
    }
*/



}
