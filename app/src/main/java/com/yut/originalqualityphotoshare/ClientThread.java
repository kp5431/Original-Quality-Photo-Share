package com.yut.originalqualityphotoshare;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.Socket;

public class ClientThread implements Runnable{

    private String[] fileLocations;
    private Socket sock;



    public ClientThread(String[] files, Socket sock){
        this.fileLocations= files;
        this.sock= sock;
    }

    public void run(){

        try {

            DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
            System.out.println("number of files: "+ fileLocations.length);
            dos.writeInt(fileLocations.length); //tells client number of files to create


            for (String input : fileLocations) {
                System.out.println("file location: "+ input);
                File file = new File(input); //file object
                byte[] toBytes = new byte[(int) file.length()]; //converts file to bytes for outputstream
                System.out.println("byte[] length: "+toBytes.length);

                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
                dis.readFully(toBytes, 0, toBytes.length); //reads file into byte array

                dos.writeUTF(file.getName()); //sends filename
                dos.writeLong(toBytes.length); //sends length of file
                dos.write(toBytes, 0, toBytes.length); //sends file
                dos.writeUTF("file finished"); //sends marker for file finished
                dos.flush();

                dis.close();

            }
            //closes all

            dos.close();
            sock.close();

        }
        catch(IOException io){
            System.out.println("error within streams");
        }

    }
}

