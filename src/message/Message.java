package message;

/**
 * Created by archer on 2016-10-06.
 */
public class Message {

    private Signal signal;
    private String supplementalData;

    public enum Signal {
        INVITED,
        TRO,
        BYE,
        OK,
        ACK,
        ERROR,
        //internal signal not for network
        SHUTDOWN,
        INVITE


    }


    public Message(String supplementalData, Signal message) {
        this.supplementalData = supplementalData;
        this.signal = message;
    }

    public Signal getSignal() {
        return signal;
    }

    public String getSupplementalData() {
        return supplementalData;
    }
}