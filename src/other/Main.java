package other;

import message.InternalMessage;
import message.MessagePasser;
import message.NetworkMessage;
import network.NetworkHandler;
import states.Available;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) throws IOException {
        MessagePasser<NetworkMessage> networkPasser = new MessagePasser(new ConcurrentHashMap<Object,Consumer<NetworkMessage>>());
        MessagePasser<InternalMessage> internalPasser = new MessagePasser(new ConcurrentHashMap<Object,Consumer<InternalMessage>>());
        StateHandler sth = new StateHandler(new Available(), new AudioStreamUDP(),networkPasser,internalPasser);
        NetworkHandler nh = new NetworkHandler(6666, networkPasser);
        try {
            nh.init();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
        new Thread(nh).start();

        //AudioStreamUDP audioStreamUDP = new AudioStreamUDP();

        try{
            Thread.sleep(5000);
        }catch (InterruptedException e ){

        }

       // aus.stopStreaming();

    }
}
