package com.yut.originalqualityphotoshare;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;




public class qrGeneratorActivity extends AppCompatActivity {

    String ipaddress;
    String port;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);
        ipaddress=getIpAddress();
        this.port="6969";
        this.imageView= findViewById(R.id.qrCodeImageView);

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
}
