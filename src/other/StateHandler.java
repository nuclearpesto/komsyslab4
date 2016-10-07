package other;

import other.Message.Signal;
import states.State;

import static other.Message.Signal.ERROR;

/**
 * Created by archer on 2016-10-06.
 */
public class StateHandler {
    private static State currentState;
    private static AudioStreamUDP aus;
    private static MessagePasser mp;
    public StateHandler(State initialState, AudioStreamUDP aus,MessagePasser mp){
        this.currentState = initialState;
        this.aus = aus;
        this.mp = mp;
        mp.register(this,k -> consumeMessage(k));
    }

    public static State getCurrentState() {
        return currentState;
    }
    public void printCurrentState(){
        System.out.println(currentState);
    }
    public static AudioStreamUDP getAus(){
        return aus;
    }

    public void consumeMessage(Message m) throws ThisShouldNeverHappenException {
        System.out.println(m.getMessage().toString());
        switch (m.getMessage()){
            case INVITE:
                currentState = currentState.invite();
                break;
            case BYE:
                currentState = currentState.bye();
                break;
            case INVITED:
                currentState = currentState.invited(m.getSupplementalData());
                mp.sendMessage(this,new Message("5000", Message.Signal.TRO));
                System.out.println("DOING STUFF");
                break;
            case TRO:
                currentState = currentState.TRO(m.getSupplementalData());
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
