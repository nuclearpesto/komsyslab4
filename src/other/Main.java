package other;

import Network.NetworkHandler;
import states.Available;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        StateHandler sth = new StateHandler(new Available());
        NetworkHandler nh = new NetworkHandler(6666, sth);
        try {
            nh.init();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
        new Thread(nh).start();
        AudioStreamUDP aus = new AudioStreamUDP();
        InetAddress addr = InetAddress.getByName("130.229.168.62");
        System.out.println(aus.getLocalPort());

        Scanner sc = new Scanner(System.in);
        int port = sc.nextInt();
        aus.connectTo(addr,port);
        System.out.println(aus.getLocalPort());
        aus.startStreaming();



        try{
            Thread.sleep(5000);
        }catch (InterruptedException e ){

        }
       // aus.stopStreaming();

    }
}
