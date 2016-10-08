package message;

/**
 * Created by archer on 2016-10-06.
 */
public class Message {

    private Signal message;
    private String supplementalData;

    public enum Signal {
        INVITED,
        TRO,
        BYE,
        OK,
        ACK,
        ERROR,
        //internal message not for network
        SHUTDOWN,
        INVITE


    }

    public Message(String supplementalData, Signal message) {
        this.supplementalData = supplementalData;
        this.message = message;
    }

    public Signal getMessage() {
        return message;
    }

    public String getSupplementalData() {
        return supplementalData;
    }
}