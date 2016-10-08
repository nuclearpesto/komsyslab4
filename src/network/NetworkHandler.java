package network;

import message.Message;
import message.NetworkMessage;
import message.MessagePasser;
import other.StateHandler;

import java.io.IOException;
import java.net.*;
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
    private MessagePasser<NetworkMessage> mp;

    public NetworkHandler(int localport,  MessagePasser<NetworkMessage> mp) {
        this.port = localport;
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


    public void sendMessage(NetworkMessage msg) {
        if (msg.getSocketAddress() == null) {
            System.out.println("No client in msg");
            // dont throw exception because of lambdas
            return;
        }
        String res = null;
        try {
            res = messageToNet(msg);
        } catch (MalformedMessageException e) {
            System.out.println("Malformed message");
            e.printStackTrace();
            return;
        }
        DatagramPacket p = new DatagramPacket(outputBuf, buffsize);
        p.setData(res.getBytes());
        p.setSocketAddress(msg.getSocketAddress());
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
        byte[] b = Arrays.copyOfRange(recv.getData(), 0, recv.getLength());
        String recieved = new String(b, StandardCharsets.UTF_8).trim();
        String msgType;
        String suplementalData= "";
        if (recieved.indexOf(":") != -1) {
            msgType = recieved.substring(0, recieved.indexOf(":"));
            suplementalData = recieved.substring(recieved.indexOf(":"));
        } else {
            msgType = recieved;
        }


        switch (msgType) {
            case "INVITE":
                mp.sendMessage(this,new NetworkMessage(suplementalData, Message.Signal.INVITED, new InetSocketAddress(recv.getAddress(), recv.getPort())));
                //dosuccess
                break;
            case "BYE":
                mp.sendMessage(this,new NetworkMessage(suplementalData, Message.Signal.BYE, new InetSocketAddress(recv.getAddress(),recv.getPort())));
                break;
            case "TRO":
                mp.sendMessage(this, new NetworkMessage(suplementalData, Message.Signal.TRO, new InetSocketAddress(recv.getAddress(),recv.getPort())));
                break;
            case "ACK":
                mp.sendMessage(this, new NetworkMessage(suplementalData, Message.Signal.ACK, new InetSocketAddress(recv.getAddress(),recv.getPort())));
                break;
            case "OK":
                mp.sendMessage(this, new NetworkMessage(suplementalData, Message.Signal.OK, new InetSocketAddress(recv.getAddress(),recv.getPort())));
                break;
            case "ERROR":
                mp.sendMessage(this, new NetworkMessage(suplementalData, Message.Signal.ERROR, new InetSocketAddress(recv.getAddress(),recv.getPort())));
                break;
            default:
                //do error
                break;
        }

    }

    public String messageToNet(NetworkMessage m) throws MalformedMessageException {
        //if invite/tro should validate correct data
        String res = m.getSignal().toString();

        /*if (m.getSignal().equals(Message.Signal.INVITE) || m.getSignal().equals(Message.Signal.TRO)) {
            try {
                data = Integer.parseInt(m.getSupplementalData());
                if (data <= 1024 || data > Short.MAX_VALUE) {
                    throw new MalformedMessageException("port invalid");
                }
            } catch (NumberFormatException e) {
                throw new MalformedMessageException(e);
            }
            res += ":" + m.getSupplementalData();
        }*/
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
