package states;

/**
 * Created by archer on 2016-10-06.
 */
public class RingingInvited extends State{

    @Override
    public State ack() {
        return new Streaming();

    }
}
