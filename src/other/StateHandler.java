package other;

import states.Available;
import states.State;

import static java.lang.Thread.sleep;

/**
 * Created by archer on 2016-10-06.
 */
public class StateHandler {
    private static State currentState;
    private static AudioStreamUDP aus;
    private static MessagePasser mp;
    private static int timeout = 5000;
    private Thread timerThread;
    private Timer timerRunnable;

    public StateHandler(State initialState, AudioStreamUDP aus, MessagePasser mp) {
        this.currentState = initialState;
        this.aus = aus;
        this.mp = mp;
        mp.register(this, k -> consumeMessage(k));
    }

    public State getCurrentState() {
        return currentState;
    }

    public void printCurrentState() {
        System.out.println(currentState);
    }

    public AudioStreamUDP getAus() {
        return aus;
    }

    public void consumeMessage(Message m) throws ThisShouldNeverHappenException {
        stopTimer();
        synchronized (currentState) {
            System.out.println(m.getMessage().toString());
            switch (m.getMessage()) {
                case INVITE:
                    currentState = currentState.invite(this);
                    break;
                case BYE:
                    currentState = currentState.bye(this);
                    break;
                case INVITED:
                    currentState = currentState.invited(this, m.getSupplementalData());
                    mp.sendMessage(this, new Message("5000", Message.Signal.TRO));
                    System.out.println("DOING STUFF");
                    break;
                case TRO:
                    currentState = currentState.TRO(this, m.getSupplementalData());
                    break;
                case ACK:
                    currentState = currentState.ack(this);
                    break;
                case OK:
                    currentState = currentState.ok(this);
                    break;
                case SHUTDOWN:
                    currentState = currentState.shutDown(this);
                    break;
                case ERROR:
                    currentState = currentState.error(this);
                    break;
                default:
                    throw new ThisShouldNeverHappenException();

            }
            printCurrentState();
        }
    }

    public void sendInvite() {
        // TODO: 2016-10-08 implement
    }

    public void sendBye() {

    }

    public void sendTro() {

    }

    public void sendAck() {

    }

    public void sendOK() {

    }

    public void sendError() {

    }

    public synchronized void setTimer(State requester) {
        stopTimer();
        timerRunnable = new Timer(timeout, requester, this);
        timerThread = new Thread(timerRunnable);
        timerThread.start();


    }

    public synchronized void stopTimer() {
        if (timerThread != null &&
                timerRunnable != null &&
                (!timerThread.getState().equals(Thread.State.TERMINATED)
                        || timerThread.getState().equals(Thread.State.NEW))) {
            timerRunnable.setDestroy(true);
            timerThread.interrupt();
        }
    }


    private class Timer implements Runnable {
        private long starttime = 0;
        private long timeout = 0;
        private State requester;
        private StateHandler sth;
        private boolean destroy = false;

        public Timer(long timeout, State requester, StateHandler sth) {
            this.starttime = starttime;
            this.timeout = timeout;
            this.requester = requester;
            this.sth = sth;
        }

        public void setDestroy(boolean destroy) {
            this.destroy = destroy;
        }


        @Override
        public void run() {
            while (System.currentTimeMillis() < (starttime + timeout)) {
                try {
                    sleep(Math.abs(starttime + timeout - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    //we have been canceled
                    if (destroy) {
                        return;
                    }
                }
            }

            if (getCurrentState() != requester) {
                return;
            } else {
                synchronized (currentState) {
                    currentState = requester.timeout(sth);
                }

            }

        }

    }


}
