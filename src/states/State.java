package states;

/**
 * Created by archer on 2016-10-06.
 */
public abstract class State {


    public State TRO() {
        return this;
    }

    public State invite() {
        return this;
    }
    public State invited(){
        return this;
    }

    public State ack() {
        return this;
    }

    public State bye() {
        return this;
    }

    public State ok() {
        return this;
    }

    public State shutDown(){
        return this;
    }
    public State error(){
        return this;
    }
}
