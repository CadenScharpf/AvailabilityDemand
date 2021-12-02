package AvailabilityDemand;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer implements ISubscriber {

	private MessageBus messageBus;
	private List<String> outputBuffer;
	private String name;
	private Room[] room;

	public Customer(String name,MessageBus messageBus, List<String> outputBuffer)
	{
		this.name = name;
		this.messageBus = messageBus;
		this.outputBuffer = outputBuffer;
	}

	/**
	 * @see ISubscriber#subscribe(String, Date, Date)
	 */
	public boolean subscribe(String location, Date from, Date to) {
		return messageBus.subscribe(this, this.name, location, from, to);
	}


	/**
	 * @see ISubscriber#unSubscribe(String, Date, Date)
	 */
	public boolean unSubscribe(String location, Date from, Date to) {
		messageBus.unsubscribe(this, this.name, location, from, to);
		return true;
	}

	/**
	 * @see ISubscriber#getName()
	 */
	public String getName(){return this.name;}
	/**
	 * @see ISubscriber#notify(String)
	 */
	public void notify(String message) {
		//[bnbOwner, location, from date, to date]
		String[] o = message.split(",");
		outputBuffer.add(this.name + " notified of B&B availability in " + o[1] + " from " + o[2] + " to " + o[3] + " by " + o[0] + " B&B");
	}
}
