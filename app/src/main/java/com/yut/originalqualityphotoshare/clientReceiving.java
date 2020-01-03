package com.yut.originalqualityphotoshare;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;

import java.io.DataInputStream;


import java.io.FileOutputStream;
import java.io.IOException;

import java.net.Socket;



public class clientReceiving extends AsyncTask<String[],Void,Void> {

    Socket sock;

    @Override
    protected Void doInBackground(String[]... incoming) {
        String[] ipAndport= incoming[0];
        int numfFiles;


        try {
            this.sock = new Socket(ipAndport[0],Integer.parseInt(ipAndport[1]));
            System.out.println("connected");
        }
        catch (IOException e){
            System.out.println("IO exception at socket creation");
            e.getCause();
        }
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(sock.getInputStream()));

            numfFiles = dis.readInt();
            System.out.println("Number of files to be received: " + numfFiles);
            for (int i = 0; i < numfFiles; i++) {
                String filename = Environment.getExternalStorageDirectory() + "/" + "OQPS" + dis.readUTF(); //appends current filename to the OQPS directory
                System.out.println("Current filename: " + filename);
                FileOutputStream fos = new FileOutputStream(filename);

                byte[] bytes = new byte[(int) dis.readLong()];
                System.out.println("byte[] length: " + bytes.length);
                dis.readFully(bytes, 0, bytes.length);
                System.out.println("file finished: " + dis.readUTF());
                fos.write(bytes, 0, bytes.length); //writes file to OQPS directory


            }
        }
        catch(IOException e){
            System.out.println("io exception within file receiving loop");
            e.getCause();
        }
        return null;
    }
}
