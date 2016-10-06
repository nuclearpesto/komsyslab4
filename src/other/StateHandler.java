package other;

import states.State;

/**
 * Created by archer on 2016-10-06.
 */
public class StateHandler {
    private static State currentState;

    public StateHandler(State initialState){
        this.currentState = initialState;
    }

    public static State getCurrentState() {
        return currentState;
    }
    public void printCurrentState(){
        System.out.println(currentState);
    }

    public void consumeMessage(Message m) throws ThisShouldNeverHappenException {
        switch (m){
            case INVITE:
                currentState = currentState.invite();
                break;
            case BYE:
                currentState = currentState.bye();
                break;
            case INVITED:
                currentState = currentState.invited();
                break;
            case TRO:
                currentState = currentState.TRO();
                break;
            case ACK:
                currentState = currentState.ack();
                break;
            case OK:
                currentState = currentState.ok();
                break;
            case SHUTDOWN:
                currentState = currentState.shutDown();
                break;
            case ERROR:
                currentState = currentState.error();
                break;
            default:
                throw new ThisShouldNeverHappenException();

        }
        printCurrentState();

    }


}
