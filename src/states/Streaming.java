package states;

/**
 * Created by archer on 2016-10-06.
 */
public class Streaming extends State {


    @Override
    public State bye() {
        //send ok
        return new Available();
    }

    @Override
    public State shutDown() {
        //send BYE
        return new ShutDown();
    }
}
