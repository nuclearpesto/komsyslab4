package other;

import Network.NetworkHandler;

import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by archer on 2016-10-07.
 */
public class MessagePasser implements Runnable{
    public ConcurrentHashMap<Object,Consumer<Message>> stateFunctions;

    public MessagePasser() {
        this.stateFunctions = new ConcurrentHashMap<>();
    }

    public void register(Object sender, Consumer<Message> func){
        stateFunctions.put(sender,func);
    }

    public void sendMessage(Object o,Message m){
        System.out.println("trying to send message" + stateFunctions.keySet());
        for(Object obj : stateFunctions.keySet()){
            if(!obj.equals(o)){
                System.out.println("sending message to o" + obj.toString());
                stateFunctions.get(obj).accept(m);
            }
        }
    }

    public void deregister(Object o){
        stateFunctions.remove(o);
    }

    @Override
    public void run() {

    }
}
