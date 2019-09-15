package com.yut.originalqualityphotoshare;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class serverSending extends AsyncTask <String[], String, Long>  {

   private ServerSocket serverSocket;


    @Override
    protected Long doInBackground(String[]... incoming) {
       String[] files=incoming[0];
        try {
            serverSocket = new ServerSocket(6969);
            publishProgress("server socket created");
        } catch (IOException io) {
            publishProgress("server socket failed");
            io.printStackTrace();
        }
        while (!serverSocket.isClosed()) {       //may be incorrect boolean, should be while server is running
            try {
                Socket clientSocket = serverSocket.accept();
                publishProgress("client connection established");
                ClientThread client = new ClientThread(files, clientSocket);
                Thread thread = new Thread(client);
                thread.start();
                publishProgress("clientThread started");


            } catch (IOException io) {
                publishProgress("failed to create client socket");

                io.printStackTrace();
            }
        }

        return null;
    }
}