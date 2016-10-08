package states;

import other.StateHandler;

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
        stateHandler.getAus().startStreaming();
        return new Streaming();

    }
}
