package states;

/**
 * Created by archer on 2016-10-06.
 */
public class ShutDown  extends State{

    @Override
    public State ok() {
        return new Available();
    }
}
