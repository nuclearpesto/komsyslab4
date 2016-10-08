package other;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
       /* MessagePasser mp = new MessagePasser();
        StateHandler sth = new StateHandler(new Available(), new AudioStreamUDP(),mp);
        NetworkHandler nh = new NetworkHandler(6666, sth,mp);
        try {
            nh.init();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
        new Thread(nh).start();
        */
        AudioStreamUDP audioStreamUDP = new AudioStreamUDP();

        try{
            Thread.sleep(5000);
        }catch (InterruptedException e ){

        }

       // aus.stopStreaming();

    }
}
