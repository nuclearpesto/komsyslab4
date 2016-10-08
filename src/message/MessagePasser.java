package message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created by archer on 2016-10-07.
 */
public class MessagePasser<T extends Message>{
    public ConcurrentHashMap<Object,Consumer<T>> stateFunctions;

    public MessagePasser() {
        this.stateFunctions = new ConcurrentHashMap<>();
    }

    public void register(Object sender, Consumer<T> func){
        stateFunctions.put(sender,func);
    }

    public void sendMessage(Object o, T t){
        System.out.println("trying to send message" + stateFunctions.keySet());
        for(Object obj : stateFunctions.keySet()){
            if(!obj.equals(o)){
                System.out.println("sending message to o" + obj.toString());
                stateFunctions.get(obj).accept(t);
            }
        }
    }

    public void deregister(Object o){
        stateFunctions.remove(o);
    }
}
