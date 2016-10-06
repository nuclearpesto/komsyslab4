package other;

import Network.NetworkHandler;
import states.Available;

import java.net.SocketException;

public class Main {

    public static void main(String[] args) {

        StateHandler sth = new StateHandler(new Available());
        NetworkHandler nh = new NetworkHandler(6666, sth);
        try {
            nh.init();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
        new Thread(nh).start();


    }
}
