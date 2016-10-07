package other;

import Network.NetworkHandler;
import states.Available;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

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
        aus.connectTo( Inet4Address.getLocalHost(),9999);
        aus.startStreaming();
        AudioStreamUDP aus2 = new AudioStreamUDP();
        aus2.connectTo( Inet4Address.getLocalHost(),9999);
        aus2.startStreaming();



        try{
            Thread.sleep(5000);
        }catch (InterruptedException e ){

        }
        aus.stopStreaming();
        aus2.stopStreaming();

    }
}
