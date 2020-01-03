package com.yut.originalqualityphotoshare;



import java.net.InetAddress;
import java.net.DatagramSocket;

public class NetworkThread extends Thread {
    private String ipaddress;



    public NetworkThread(){

    }
    @Override
    public void run() {
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ipaddress = socket.getLocalAddress().getHostAddress();
        }
        catch (Exception e){
            System.out.println("networkthreadexception");
        }


    }

    public String getIpaddress(){
        return ipaddress;
    }
}
