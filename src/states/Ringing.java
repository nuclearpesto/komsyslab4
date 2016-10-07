package states;

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
    public State TRO(String suplementalData) {
        //send ack
        return new Streaming();

    }
}
