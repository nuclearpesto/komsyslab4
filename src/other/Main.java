package other;

import Network.NetworkHandler;
import states.Available;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        MessagePasser mp = new MessagePasser();
        StateHandler sth = new StateHandler(new Available(), new AudioStreamUDP(),mp);
        NetworkHandler nh = new NetworkHandler(6666, sth,mp);
        try {
            nh.init();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
        new Thread(nh).start();

        AudioStreamUDP audioStreamUDP = new AudioStreamUDP();

        Scanner sc = new Scanner(System.in);
        System.out.println("enter ip");
        String ip = sc.nextLine();
        System.out.println("enter port");
        int port = sc.nextInt();

        audioStreamUDP.connectTo(InetAddress.getByName(ip),port);


        try{
            Thread.sleep(5000);
        }catch (InterruptedException e ){

        }

       // aus.stopStreaming();

    }
}
