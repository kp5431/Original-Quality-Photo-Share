package com.yut.originalqualityphotoshare;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThread implements Runnable{

    private String[] fileLocations;
    private Socket sock;



    public ClientThread(String[] files, Socket sock){
        this.fileLocations= files;
        this.sock= sock;
    }

    public void run(){
        /*
         */
        byte[] filefinished= new byte[3];
        String str= "done";
        filefinished= str.getBytes();

        try {
            OutputStream os = sock.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(fileLocations.length); //tells client number of files to create


            for (String input : fileLocations) {
                File file = new File(input); //file object
                byte[] toBytes = new byte[(int) file.length()]; //converts file to bytes for outputstream
                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
                dis.readFully(toBytes, 0, toBytes.length);

                dos.writeUTF(file.getName());
                dos.writeLong(toBytes.length);
                dos.write(toBytes, 0, toBytes.length);
                dos.write(filefinished, 0, 3);
                dos.flush();

                dis.close();

            }
            //closes all
            os.close();
            dos.close();
            sock.close();

        }
        catch(IOException io){
            System.out.println("error within streams");
        }

    }
}

