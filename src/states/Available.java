package states;

import other.StateHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by archer on 2016-10-06.
 */
public class Available extends State {

    @Override
    public State invite(StateHandler stateHandler, InetSocketAddress clientAddress) {
        //send invite

        stateHandler.setClientAddress(clientAddress);
        State nextState = new Ringing();
        stateHandler.sendInvite();
        stateHandler.setTimer(nextState);


        return nextState;
    }

    @Override
    public State invited(StateHandler stateHandler, InetSocketAddress clientAddress) {
        //save socket of client
        //send TRO
        stateHandler.setClientAddress(clientAddress);
        stateHandler.sendTro();

        return new RingingInvited();
    }

    @Override
    public String toString() {
        return "Available{}";
    }
}

