package states;

import other.StateHandler;

/**
 * Created by archer on 2016-10-06.
 */
public class ShutDown  extends State{

    @Override
    public State ok(StateHandler stateHandler) {
        stateHandler.setClientAddress(null);
        return new Available();
    }

    @Override
    public State timeout(StateHandler stateHandler) {
        stateHandler.setClientAddress(null);
        return new Available();

    }

    @Override
    public State error(StateHandler stateHandler) {
        stateHandler.setClientAddress(null);
        return new Available();
    }

    @Override
    public String toString() {
        return "ShutDown{}";
    }


}
