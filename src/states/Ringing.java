package states;

import other.StateHandler;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
    public State TRO(StateHandler stateHandler, String suplementalData) {
        String[]  parts = suplementalData.split(":");

        InetAddress addr;
        int port;

        try{
            addr = InetAddress.getByName(parts[0]);
            port = Integer.parseInt(parts[1]);
            stateHandler.getAus().connectTo(addr,port);
        }catch (Exception e) {
            //for indexoutofbounds, unknownhost, IO and nubmberformat
            stateHandler.sendError();
            return new Available();
        }

        //send ack
        stateHandler.sendAck();
        stateHandler.getAus().startStreaming();
        return new Streaming();

    }

    @Override
    public State timeout(StateHandler stateHandler) {
        //send error
        stateHandler.sendError();
        return new Available();

    }
}
