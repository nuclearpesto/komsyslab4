package states;

import other.StateHandler;

/**
 * Created by archer on 2016-10-06.
 */
public class Streaming extends State {


    @Override
    public State bye(StateHandler stateHandler) {
        //send ok
        stateHandler.getAus().stopStreaming();
        stateHandler.sendOK();
        stateHandler.setClientAddress(null);
        return new Available();
    }

    @Override
    public State shutDown(StateHandler stateHandler) {
        //send BYE
        stateHandler.getAus().stopStreaming();
        stateHandler.sendBye();
        State nextState = new ShutDown();
        stateHandler.setTimer(nextState);
        return nextState;
    }

    @Override
    public State error(StateHandler stateHandler) {
        stateHandler.getAus().stopStreaming();
        stateHandler.setClientAddress(null);
        return new Available();

    }

    @Override
    public String toString() {
        return "Streaming{}";
    }
}
