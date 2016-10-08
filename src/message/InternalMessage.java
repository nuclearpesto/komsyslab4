package message;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by archer on 2016-10-08.
 */
public class InternalMessage extends Message {
   private InetSocketAddress socketAddress;

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public InternalMessage(String supplementalData, Signal message, InetSocketAddress socketAddress) {
        super(supplementalData, message);
        this.socketAddress = socketAddress;
    }
}
