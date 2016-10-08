package states;

import other.Message;
import other.StateHandler;

import java.net.InetAddress;

/**
 * Created by archer on 2016-10-06.
 */
public class Available extends State {

    @Override
    public State invite(StateHandler stateHandler) {
        //send invite
        State nextState = new Ringing();
        stateHandler.sendInvite();
        stateHandler.setTimer(nextState);


        return nextState;
    }

    @Override
    public State invited(StateHandler stateHandler, String suplementalData) {
        //save socket of client
        //send TRO
        String[] parts = suplementalData.split(":");
        try {
            InetAddress addr = InetAddress.getByName(parts[0]);
            int port = Integer.parseInt(parts[1]);
            stateHandler.getAus().connectTo(addr, port);
        } catch (Exception e) {
            //for indexoutofbounds, unknownhost, IO and nubmberformat
            stateHandler.sendError();
            return this;
        }
        stateHandler.sendTro();

        return new RingingInvited();
    }

    @Override
    public String toString() {
        return "Available{}";
    }
}

