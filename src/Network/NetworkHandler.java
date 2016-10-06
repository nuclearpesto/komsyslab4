package Network;

import other.Message;
import other.StateHandler;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by archer on 2016-10-06.
 */
public class NetworkHandler implements Runnable{
    DatagramSocket d;
    SocketAddress clientAddress;
    StateHandler stha;
    int port;
    int buffsize = 5000;
    byte[] inputBuf = new byte[buffsize];
    byte[] outputBuf = new byte[buffsize];
    private boolean inited = false;
    private boolean shutdown = false;


    public NetworkHandler(int localport, StateHandler stha){
        this.port = localport;
        this.stha =stha;
    }

    public void setClientAddress(SocketAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    public void init() throws SocketException {
        d = new DatagramSocket(port);
    }


    public void sendMessage(String str) throws NoClientSpecifiedException {
            if(clientAddress == null){
                throw new NoClientSpecifiedException("no client set in networkhandler");
            }
            DatagramPacket p = new DatagramPacket(outputBuf, buffsize);
            p.setSocketAddress(clientAddress);
        try {
            d.send(p);
        } catch (IOException e) {
            // TODO: 2016-10-06 should kill program
            e.printStackTrace();
        }


    }

    private void recieveMsg() throws IOException {
        DatagramPacket recv  = new DatagramPacket(inputBuf, buffsize);
        d.receive(recv);
        byte[] b = Arrays.copyOfRange(recv.getData(), 0, recv.getLength());
        String  recieved = new String(b, StandardCharsets.UTF_8).trim();
        switch(recieved){
              case "INVITE":
                  System.out.println("CONSUME INVITE");
                  stha.consumeMessage(Message.INVITE);
                break;
            case "BYE":
                stha.consumeMessage(Message.BYE);
                break;
            case "INVITED":
                stha.consumeMessage(Message.INVITED);
                break;
            case "TRO":
                stha.consumeMessage(Message.TRO);
                break;
            case "ACK":
                stha.consumeMessage(Message.ACK);
                break;
            case "OK":
                stha.consumeMessage(Message.OK);
                break;
            case "SHUTDOWN":
                stha.consumeMessage(Message.SHUTDOWN);
                break;
            case "ERROR":
                stha.consumeMessage(Message.ERROR);
                break;
            default:
                //do error
                break;
        }
    }


    @Override
    public void run() {

        //skapa ny socket
        while(!shutdown){
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
