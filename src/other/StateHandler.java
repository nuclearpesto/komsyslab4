package other;

import com.sun.org.apache.xpath.internal.SourceTree;
import states.State;

import static java.lang.Thread.sleep;

import message.*;

import java.net.InetSocketAddress;

/**
 * Created by archer on 2016-10-06.
 */
public class StateHandler {
    private static State currentState;
    private static AudioStreamUDP aus;
    private static MessagePasser<NetworkMessage> networkPasser;
    private MessagePasser<InternalMessage> internalPasser;
    private static int timeout = 5000;
    private Thread timerThread;
    private Timer timerRunnable;
    private InetSocketAddress clientAddress = null;

    public StateHandler(State initialState, AudioStreamUDP aus, MessagePasser<NetworkMessage> networkPasser, MessagePasser<InternalMessage> internalPasser) {
        this.currentState = initialState;
        this.aus = aus;
        this.networkPasser = networkPasser;
        this.internalPasser = internalPasser;
        networkPasser.register(this, k -> consumeNetworkMessage(k));
        internalPasser.register(this, k -> consumeInternalMessage(k));
    }

    public State getCurrentState() {
        return currentState;
    }

    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(InetSocketAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    public void printCurrentState() {
        System.out.println("State is now: " + currentState);
    }

    public AudioStreamUDP getAus() {
        return aus;
    }

    public void consumeInternalMessage(InternalMessage m) {
        synchronized (currentState) {
            switch (m.getSignal()) {
                case INVITE:
                    System.out.println(m.getSocketAddress().toString());
                    currentState = currentState.invite(this, m.getSocketAddress());
                    break;
                case SHUTDOWN:
                    currentState = currentState.shutDown(this);
                    break;
                default:
                    return;

            }
            printCurrentState();
        }
    }


    public void consumeNetworkMessage(NetworkMessage m) throws ThisShouldNeverHappenException {
        stopTimer();

        synchronized (currentState) {
            System.out.println(m.getSignal().toString());
            if(clientAddress != null && !m.getSocketAddress().equals(clientAddress)){
                System.out.println("got command from somewhere other than client");
                return;
            }
            switch (m.getSignal()) {
                case BYE:
                    currentState = currentState.bye(this);
                    break;
                case INVITED:
                    currentState = currentState.invited(this, m.getSocketAddress());
                    break;
                case TRO:
                    currentState = currentState.TRO(this);
                    break;
                case ACK:
                    currentState = currentState.ack(this);
                    break;
                case OK:
                    currentState = currentState.ok(this);
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
        System.out.println("HELLÖ" + clientAddress.toString());
        NetworkMessage networkMessage = new NetworkMessage(null, Message.Signal.INVITE, clientAddress);
        System.out.println(networkMessage);
        networkPasser.sendMessage(this, networkMessage);
    }

    public void sendBye() {
        networkPasser.sendMessage(this, new NetworkMessage(null, Message.Signal.BYE, clientAddress));
    }

    public void sendTro() {
        networkPasser.sendMessage(this, new NetworkMessage(null, Message.Signal.TRO, clientAddress));
    }

    public void sendAck() {
        networkPasser.sendMessage(this, new NetworkMessage(null, Message.Signal.ACK, clientAddress));
    }

    public void sendOK() {
        networkPasser.sendMessage(this, new NetworkMessage(null, Message.Signal.OK, clientAddress));
    }

    public void sendError() {
        System.out.println("IN SEND ERROR "+clientAddress.toString());
        networkPasser.sendMessage(this, new NetworkMessage(null, Message.Signal.ERROR, clientAddress));
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
            this.timeout = timeout;
            this.requester = requester;
            this.sth = sth;
        }

        public void setDestroy(boolean destroy) {
            this.destroy = destroy;
        }


        @Override
        public void run() {
            starttime = System.currentTimeMillis();
            while (System.currentTimeMillis() < (starttime + timeout)) {
                try {
                    System.out.println("TimeoutTime, currentime" + (starttime+timeout)+ " : " + System.currentTimeMillis());
                    sleep(Math.abs(starttime + timeout - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    //we have been canceled
                    System.out.println("TIMER INTERRUPTED DESTROY IS " + destroy);
                    if (destroy) {
                        return;
                    }
                }
            }

            synchronized (currentState) {
                System.out.println("TIMED OUT FOR REQUESTER: " + requester);
                if (getCurrentState() != requester) {
                    return;
                } else {
                    currentState = requester.timeout(sth);
                    printCurrentState();
                }
            }

        }

    }


}
