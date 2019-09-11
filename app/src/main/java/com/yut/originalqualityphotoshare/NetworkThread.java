package com.yut.originalqualityphotoshare;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkThread extends Thread {
    private String ipaddress;


    public NetworkThread(){

    }
    @Override
    public void run() {
        try {
            this.ipaddress = InetAddress.getLocalHost().toString();
        }
        catch (UnknownHostException e){
            ipaddress="Unknown Host";
        }
    }

    public String getIpaddress(){
        return ipaddress;
    }
}
