package states;

import other.StateHandler;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by archer on 2016-10-06.
 */
public class Ringing extends State {

    @Override
    public String toString() {
        return "Ringing{}";
    }

    public Ringing() {

    }

    @Override
    public State TRO(StateHandler stateHandler) {
        //send ack
        stateHandler.sendAck();
        try {
            stateHandler.getAus().connectTo(stateHandler.getClientAddress().getAddress(), stateHandler.getClientAddress().getPort()+1);
        } catch (IOException e) {
            stateHandler.sendError();
            return new Available();
        }
        stateHandler.getAus().startStreaming();
        return new Streaming();

    }

    @Override
    public State timeout(StateHandler stateHandler) {
        //send error
        stateHandler.sendError();
        //not communicating with client
        stateHandler.setClientAddress(null);
        return new Available();

    }
}
