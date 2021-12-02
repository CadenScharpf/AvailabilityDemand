package AvailabilityDemand;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

public class AvailabilityDemand {

    Hashtable<String, IPublisher> publishers;
    Hashtable<String, ISubscriber> subscribers;
    List<String> outputBuffer;
    MessageBus messageBus;

    public AvailabilityDemand() {
        publishers = new Hashtable<String, IPublisher>();
        subscribers = new Hashtable<String, ISubscriber>();
        outputBuffer = new ArrayList<String>();
        messageBus = new MessageBus();
    }

    public void processInput(String command) {
        try {
            String[] args = command.split(", ");
            SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
            switch (args[0]) {
                case "publish":
                    if (!publishers.containsKey(args[1].toLowerCase())) {
                        IPublisher newProvider = new BnbProvider(args[1].toLowerCase(), messageBus);
                        publishers.put(args[1].toLowerCase(), newProvider);
                        newProvider.publish(args[1].toLowerCase(), args[2], f.parse(args[3]), f.parse(args[4]));
                    } else {
                        publishers.get(args[1].toLowerCase()).publish(args[1].toLowerCase(), args[2], f.parse(args[3]), f.parse(args[4]));
                    }
                    break;
                case "subscribe":
                    if (!subscribers.containsKey(args[1].toLowerCase())) {
                        ISubscriber customer = new Customer(args[1].toLowerCase(), messageBus, outputBuffer);
                        subscribers.put(args[1].toLowerCase(), customer);
                        customer.subscribe(args[2], f.parse(args[3]), f.parse(args[4]));
                    } else {
                        subscribers.get(args[1].toLowerCase()).subscribe(args[2], f.parse(args[3]), f.parse(args[4]));
                    }
                    break;
                case "unsubscribe":
                    if (!subscribers.containsKey(args[1].toLowerCase())) {
                        messageBus.unsubscribe(null, args[1].toLowerCase(), args[2], f.parse(args[3]), f.parse(args[4]));
                        break;
                    }
                    subscribers.get(args[1].toLowerCase()).unSubscribe(args[2], f.parse(args[3]), f.parse(args[4]));
                    break;
                default:
                    break;
            }
        } catch(ParseException e){}
    }

    public List<String> getAggregatedOutput() {
        return outputBuffer;
    }

    public void reset() {
        outputBuffer = new ArrayList<String>();
        publishers = new Hashtable<String, IPublisher>();
        subscribers = new Hashtable<String, ISubscriber>();
        messageBus = new MessageBus();
    }

}
