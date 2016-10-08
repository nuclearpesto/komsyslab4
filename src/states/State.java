package states;

import other.StateHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by archer on 2016-10-06.
 */
public abstract class State {

    public State timeout(StateHandler stateHandler) {
        return this;
    }

    public State TRO(StateHandler stateHandler) {
        return this;
    }

    public State invite(StateHandler stateHandler, InetSocketAddress socketAddress) {
        return this;
    }

    public State invited(StateHandler stateHandler, InetSocketAddress suplementalData) {
        return this;
    }

    public State ack(StateHandler stateHandler) {
        return this;
    }

    public State bye(StateHandler stateHandler) {
        return this;
    }

    public State ok(StateHandler stateHandler) {
        return this;
    }

    public State shutDown(StateHandler stateHandler) {
        return this;
    }

    public State error(StateHandler stateHandler) {
        return this;
    }
}
