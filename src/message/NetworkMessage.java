package message;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by archer on 2016-10-08.
 */
public class NetworkMessage extends Message{
    private InetSocketAddress s;
    public NetworkMessage(String supplementalData, Signal message, InetSocketAddress s) {
        super(supplementalData, message);
        this.s = s;

    }

    public InetSocketAddress getSocketAddress() {
        return s;
    }
}
