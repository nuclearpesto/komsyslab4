package cli;

import message.InternalMessage;
import message.Message;
import message.MessagePasser;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by archer on 2016-10-08.
 */
public class Cli implements Runnable {
    private MessagePasser<InternalMessage> internalPasser;
    private Scanner sc;

    public Cli(MessagePasser<InternalMessage> internalPasser, Scanner sc) {
        this.internalPasser = internalPasser;
        this.sc = sc;
    }


    @Override
    public void run() {
        while (true) {
            String line = sc.nextLine();

            if (line.startsWith("connect")) {
                String[] parts = line.split(":");
                if (parts.length < 3) {
                    System.out.printf("Error");
                } else {
                    try {

                        InetSocketAddress inetaddr = new InetSocketAddress(InetAddress.getByName(parts[1]), Integer.parseInt(parts[2]));
                        internalPasser.sendMessage(this, new InternalMessage(null, Message.Signal.INVITE, inetaddr));
                    } catch (Exception e) {
                        System.out.println("type correctly idiot");
                    }
                }
            } else if (line.equals("bye")) {

                internalPasser.sendMessage(this, new InternalMessage(null, Message.Signal.BYE, null));
            } else {
                System.out.println("not a command");
            }
        }
    }
}
