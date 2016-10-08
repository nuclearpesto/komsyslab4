package other;

import cli.Cli;
import message.InternalMessage;
import message.Message;
import message.MessagePasser;
import message.NetworkMessage;
import network.NetworkHandler;
import states.Available;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) throws IOException {
        MessagePasser<NetworkMessage> networkPasser = new MessagePasser(new ConcurrentHashMap<Object,Consumer<NetworkMessage>>());
        MessagePasser<InternalMessage> internalPasser = new MessagePasser(new ConcurrentHashMap<Object,Consumer<InternalMessage>>());
        StateHandler sth = new StateHandler(new Available(), new AudioStreamUDP(),networkPasser,internalPasser);
        NetworkHandler nh = new NetworkHandler(5554, networkPasser);
        Cli cli = new Cli(internalPasser,new Scanner(System.in));

        try {
            nh.init();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
        //read network
        new Thread(nh).start();
        //read cmdline
        new Thread(cli).start();

        //AudioStreamUDP audioStreamUDP = new AudioStreamUDP();

        try{
            Thread.sleep(5000);
        }catch (InterruptedException e ){

        }

       // aus.stopStreaming();

    }
}
