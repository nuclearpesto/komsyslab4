package states;

/**
 * Created by archer on 2016-10-06.
 */
public class Ringing extends State {

    @Override
    public State TRO() {
        //send ack
        return new Streaming();

    }
}
