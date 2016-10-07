package Network;

import other.Message;
import other.MessagePasser;
import other.StateHandler;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by archer on 2016-10-06.
 */
public class NetworkHandler implements Runnable {
    DatagramSocket d;
    SocketAddress clientAddress = null;
    StateHandler stha;
    int port;
    int buffsize = 5000;
    byte[] inputBuf = new byte[buffsize];
    byte[] outputBuf = new byte[buffsize];
    private boolean inited = false;
    private boolean shutdown = false;
    private static MessagePasser mp;

    public NetworkHandler(int localport, StateHandler stha, MessagePasser mp) {
        this.port = localport;
        this.stha = stha;
        this.mp = mp;
        register();
    }

    public void register() {
        mp.register(this, k -> sendMessage(k));
    }

    public void setClientAddress(SocketAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    public void init() throws SocketException {
        d = new DatagramSocket(port);
    }


    public void sendMessage(Message str) {
        if (clientAddress == null) {
            System.out.println("No client GO AWAY!");
            // dont throw exception because of lambdas
            return;
        }
        String res = null;
        try{
            res = messageToNet(str);
        }catch(MalformedMessageException e){
            System.out.println("Malformed Message");
            return;
        }
        DatagramPacket p = new DatagramPacket(outputBuf, buffsize);
        p.setData(res.getBytes());
        p.setSocketAddress(clientAddress);
        try {
            d.send(p);
        } catch (IOException e) {
            // TODO: 2016-10-06 should kill program
            e.printStackTrace();
        }


    }

    private void recieveMsg() throws IOException {
        DatagramPacket recv = new DatagramPacket(inputBuf, buffsize);
        d.receive(recv);
        clientAddress = recv.getSocketAddress();
        byte[] b = Arrays.copyOfRange(recv.getData(), 0, recv.getLength());
        String recieved = new String(b, StandardCharsets.UTF_8).trim();
        String[] parts = recieved.split(":");
        String suplementalData = null;
        if (parts.length > 1) {
            // TODO: 2016-10-07 get all data if there is more then 2 parts
            suplementalData = parts[2];
        }
        switch (parts[0]) {
            case "INVITE":
                stha.consumeMessage(new Message(suplementalData, Message.Signal.INVITED));
                //dosuccess
                break;
            case "BYE":
                stha.consumeMessage(new Message(suplementalData, Message.Signal.BYE));
                break;
            case "TRO":
                stha.consumeMessage(new Message(suplementalData, Message.Signal.TRO));
                break;
            case "ACK":
                stha.consumeMessage(new Message(suplementalData, Message.Signal.ACK));
                break;
            case "OK":
                stha.consumeMessage(new Message(suplementalData, Message.Signal.OK));
                break;
            case "ERROR":
                stha.consumeMessage(new Message(suplementalData, Message.Signal.ERROR));
                break;
            default:
                //do error
                break;
        }

    }

    public String messageToNet(Message m) throws MalformedMessageException {
        //if invite/tro should validate correct data
        String res = m.getMessage().toString();
        int data;
        if (m.getMessage().equals(Message.Signal.INVITE) || m.getMessage().equals(Message.Signal.TRO)) {
            try{
                data = Integer.parseInt(m.getSupplementalData());
                if(data <= 1024 || data > Short.MAX_VALUE){
                    throw new MalformedMessageException("port invalid");
                }
            }catch(NumberFormatException e){
                throw new MalformedMessageException(e);
            }
            res += ":" + m.getSupplementalData();
        }
        return res;

    }


    @Override
    public void run() {

        //skapa ny socket
        while (!shutdown) {
            try {
                recieveMsg();
            } catch (IOException e) {
                shutdown = true;
                e.printStackTrace();
            }
        }
        //lyssna efter nya medelanden, översätta och skicka till statehandler


    }
}
