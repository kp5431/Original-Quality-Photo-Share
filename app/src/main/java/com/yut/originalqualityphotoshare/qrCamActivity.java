package com.yut.originalqualityphotoshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class qrCamActivity extends AppCompatActivity {


    SurfaceView viewfinder;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    SurfaceHolder holder;
    TextView textView;
    int screenWidth;
    int screenHeight;
    Intent clientReceivingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_cam);

        int[] screenres= getScreenres();
        screenWidth=screenres[0];
        screenHeight=screenres[1];

        viewfinder = findViewById(R.id.camerapreview);
        this.holder= viewfinder.getHolder();
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(screenHeight, screenWidth).setAutoFocusEnabled(true).setRequestedFps(60).build();
        textView = findViewById(R.id.textView);

        clientReceivingIntent= new Intent(this, clientReceivingActivity.class);




        viewfinder.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if(ContextCompat.checkSelfPermission(qrCamActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(holder);
                    }
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                cameraSource.stop();
            }
        });



        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                /*
                this method decodes(?) the detected barcode, puts the value into qrCodes array,
                sets a textview to display the value, and vibrates the phone.
                 */
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size()!= 0) {

                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(1000);
                    clientReceivingIntent.putExtra("detected", qrCodes.valueAt(0).displayValue);
                    startActivity(clientReceivingIntent);
                    onStop();


                }
            }

        });

    }

    @Override
    protected void onStop(){
        super.onStop();
        cameraSource.stop();

    }


    @Override
    protected void onResume(){
        super.onResume();




    }

    public int[] getScreenres(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return new int[]{width,height};
    }



}
