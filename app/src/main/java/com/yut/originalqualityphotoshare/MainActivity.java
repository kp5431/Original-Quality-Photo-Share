package com.yut.originalqualityphotoshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {



    Button sendButton;
    Button receiveButton;

    Intent receiveIntent;
    Intent serverSendingIntent;
    TextView tutorialText;


    private final int CAMERA_PERMISSION_CODE=1;
    private final int WRITE_EXTERNAL_STORAGE_CODE=2;
    private final int READ_EXTERNAL_STORAGE_CODE=3;
    private static final int IMAGE_PICKER_SELECT=5;

    private Stack<String> URI_CODES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tutorialText=findViewById(R.id.tutorialText);
        this.sendButton =findViewById(R.id.sendButton);
        this.receiveButton = findViewById(R.id.receiveButton);

        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);// permission request popup


        this.receiveIntent= new Intent(this, qrCamActivity.class);
        this.serverSendingIntent= new Intent(this,serverSendingActivity.class);
        serverSendingIntent.putExtra("FILE_STRINGS",URI_CODES);

        this.URI_CODES= new Stack<String>();

        if(!makeDirectory()){
            Toast.makeText(this, "directory creation failed", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    protected void onResume() {
        super.onResume();


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                sendButton listener, checks if read storage permission has been met.
                If so, proceeds to next activity.
                */
                if (!(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
                    requestReadStoragePermission();
                }
                else {
                    //proceed to next activity
                    openGallery();
                }
            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                receiveButton listener, checks if camera and write storage permissions have been met.
                If so, proceeds to next activity
                */
                if (!(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
                    requestWriteStoragePermission();
                        if(!(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                            requestCameraPermission();
                        }
                        else{
                            //
                            Toast.makeText(MainActivity.this, "Opening Camera",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(receiveIntent); //go to qr cam activity
                        }
                }
                else {
                    if(!(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                        requestCameraPermission();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Opening Camera",
                                Toast.LENGTH_SHORT).show();
                        startActivity(receiveIntent); //go to qr cam activity
                    }
                }
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
                    }
                    startActivity(serverSendingIntent);
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
        requests camera permission handles user not accepting permission request using toast messages
         */
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, //has the user already denied the permission?
                Manifest.permission.CAMERA)) {
           //show explanation to user
            Toast.makeText(this, "Camera permission needed to scan QR codes", Toast.LENGTH_LONG).show();
            //request the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);

            // CAMERA_PERMISSION_CODE is an
            // app-defined int constant. The onRequestPermissionsResult method gets the code
        }
    }

    private void requestWriteStoragePermission() {
        /*
        requests write storage permission, handles user not accepting permission request using toast messages
         */
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //show explanation to user
            Toast.makeText(this, "Write storage permission is needed to save photos/videos", Toast.LENGTH_LONG).show();
            //request the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_CODE);
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_CODE);

            // WRITE_EXTERNAL_STORAGE_CODE is an
            // app-defined int constant. The onRequestPermissionsResult method gets the code
        }
    }

    private void requestReadStoragePermission(){
        /*
        requests read storage permission, handles user not accepting permission request immediately
         */
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //show explanation to user
            Toast.makeText(this, "Read storage permission is needed to send photos/videos", Toast.LENGTH_LONG).show();
            //request the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_CODE);
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                   READ_EXTERNAL_STORAGE_CODE);

            // READ_EXTERNAL_STORAGE_CODE is an
            // app-defined int constant. The onRequestPermissionsResult method gets the code
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case CAMERA_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case WRITE_EXTERNAL_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }
            case READ_EXTERNAL_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }
        }

    }

    public boolean makeDirectory(){
        /*
        function creates a folder for photos/videos to be stored in root of android storage. Returns true if
        folder was successfully created or already exists, false if folder creation failed.
         */
        File root = new File(Environment.getExternalStorageDirectory(),"OQPS");
        if(!root.exists()){
            return root.mkdir();
        }
        return true; //folder already exists
    }
}


