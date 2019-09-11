package com.yut.originalqualityphotoshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import android.Manifest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {



    Button sendButton;
    Button receiveButton;
    Button qrButton;
    Intent sendIntent;
    Intent receiveIntent;
    Intent serverSendingIntent;
    TextView tutorialText;


    private final int CAMERA_PERMISSION_CODE=1;
    private final int WRITE_EXTERNAL_STORAGE_CODE=1;
    private final int READ_EXTERNAL_STORAGE_CODE=1;
    private static final int IMAGE_PICKER_SELECT=5;

    private Stack<String> URI_CODES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tutorialText=findViewById(R.id.tutorialText);
        this.sendButton =findViewById(R.id.sendButton);
        this.receiveButton = findViewById(R.id.receiveButton);
        this.qrButton= findViewById(R.id.qrButton);

        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);// permission request popup


        this.sendIntent= new Intent(this, qrGeneratorActivity.class);
        this.receiveIntent= new Intent(this, qrCamActivity.class);
        this.serverSendingIntent= new Intent(this,serverSendingActivity.class);
        serverSendingIntent.putExtra("FILE_STRINGS",URI_CODES);

        this.URI_CODES= new Stack<String>();



    }

    @Override
    protected void onResume(){
        super.onResume();

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(sendIntent);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                sendButton listener, checks if camera and read storage permissions have been met.
                If so, proceeds to next activity.
                */
                if (ContextCompat.checkSelfPermission(MainActivity.this, //is camera is accessible? if so then:
                        Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED ) {
                    if(ContextCompat.checkSelfPermission(MainActivity.this, //is read storage accessible? if so, then:
                            Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED ){
                        //continue to end of method
                    }
                    else{ //if read storage isn't accessible, but camera is then:
                        requestReadStoragePermission();
                    }
                }

                else { //if camera isn't accessible, then
                    requestCameraPermission();
                    if(ContextCompat.checkSelfPermission(MainActivity.this, //is read storage is accessible? if so, then:
                            Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED ){
                        //continue to end of method
                    }
                    else{ //if read storage isn't accessible, but camera is, then:
                        requestReadStoragePermission();
                    }
                }

                Toast.makeText(MainActivity.this, "Opening Camera",
                        Toast.LENGTH_SHORT).show();
                //proceed to next activity from
                openGallery();

            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                receiveButton listener, checks if write storage permission has been met.
                If so, proceeds to next activity


                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //goto qr code scanner activity


                } else {
                    requestWriteStoragePermission();

                }
                */
                Toast.makeText(MainActivity.this, "Generating QR Code",
                        Toast.LENGTH_SHORT).show();
                startActivity(receiveIntent);

            }
        });



    }
    private void openGallery(){
        Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*"); //allows any image file type. Change * to specific extension to limit it
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Photos and Videos"), IMAGE_PICKER_SELECT);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_PICKER_SELECT) {
            if(resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for(int i = 0; i < count; i++){
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();

                        URI_CODES.push(imageUri.toString());
                        startActivity(serverSendingIntent);
                    }
                } else if(data.getData() != null) {
                    String imagePath = data.getData().getPath();
                    URI_CODES.push(imagePath);
                    startActivity(serverSendingIntent);

                }
            }
        }
    }
    private void requestCameraPermission(){
        /*
        requests camera permission, handles user not accepting permission request immediately
         */
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){//this line checks if user has already denied permission once
            //if true, then
            new AlertDialog.Builder(this) //the popup message explanation
                    .setTitle("Permission needed")
                    .setMessage("Camera permission is needed to scan QR codes.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() { //'ok' button is a listener that on click performs another permission request
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);// permission request popup
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() { //other cancel button
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();//exits AlertDialog
                        }
                    })
                    .create().show();
        }
        else{//if false, then
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void requestWriteStoragePermission(){
        /*
        requests write storage permission, handles user not accepting permission request immediately
         */
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){//this line checks if user has already denied permission once
            //if true, then
            new AlertDialog.Builder(this) //the popup message explanation
                    .setTitle("Write Permission needed")
                    .setMessage("Write permission is needed to receive photos and videos.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() { //'ok' button is a listener that on click performs another permission request
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE );// permission request popup
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() { //other cancel button
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();//exits AlertDialog
                        }
                    })
                    .create().show();
        }
        else{//if false, then
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
        }
    }


    private void requestReadStoragePermission(){
        /*
        requests read storage permission, handles user not accepting permission request immediately
         */
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){//this line checks if user has already denied permission once
            //if true, then
            new AlertDialog.Builder(this) //the popup message explanation
                    .setTitle("Read Permission needed")
                    .setMessage("Read permission is needed to send photos and videos.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() { //'ok' button is a listener that on click performs another permission request
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},READ_EXTERNAL_STORAGE_CODE );// permission request popup
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() { //other cancel button
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();//exits AlertDialog
                        }
                    })
                    .create().show();
        }
        else{//if false, then
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*
        displays the toast messages
        */
        if(requestCode== CAMERA_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Camera Permission GRANTED", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Camera Permission DENIED", Toast.LENGTH_SHORT).show();

            }
        }
        else if(requestCode== READ_EXTERNAL_STORAGE_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Read Permission GRANTED", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Read Permission DENIED", Toast.LENGTH_SHORT).show();

            }
        }
        else if(requestCode== WRITE_EXTERNAL_STORAGE_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Write Permission GRANTED", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Write Permission DENIED", Toast.LENGTH_SHORT).show();

            }
        }
    }
}


