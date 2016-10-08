package states;

import other.StateHandler;

import java.io.IOException;

/**
 * Created by archer on 2016-10-06.
 */
public class RingingInvited extends State{

    @Override
    public String toString() {
        return "RingingInvited{}";
    }

    @Override
    public State ack(StateHandler stateHandler) {
        try {
            stateHandler.getAus().connectTo(stateHandler.getClientAddress().getAddress(),stateHandler.getClientAddress().getPort());
            stateHandler.getAus().startStreaming();
        } catch (IOException e) {
            e.printStackTrace();
            stateHandler.sendError();
            return new Available();
        }
        return new Streaming();

    }
}
