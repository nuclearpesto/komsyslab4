package states;

import other.StateHandler;

import java.io.IOException;

/**
 * Created by archer on 2016-10-06.
 */
public class RingingInvited extends State {

    @Override
    public String toString() {
        return "RingingInvited{}";
    }

    @Override
    public State ack(StateHandler stateHandler) {
        try {
            stateHandler.getAus().connectTo(stateHandler.getClientAddress().getAddress(), stateHandler.getClientAddress().getPort()+1);
            stateHandler.getAus().startStreaming();
        } catch (IOException e) {
            e.printStackTrace();
            stateHandler.sendError();
            return new Available();
        }
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
