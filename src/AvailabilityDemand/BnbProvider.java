package AvailabilityDemand;

import AvailabilityDemand.MessageBus;

import java.util.Date;

public class BnbProvider implements IPublisher {

	private MessageBus messageBus;
	private String name;
	private Room room;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BnbProvider that = (BnbProvider) o;
		return name.equals(that.name);
	}

	public BnbProvider(String name, MessageBus messageBus)
	{
		this.name = name;
		this.messageBus = messageBus;
	}

	/**
	 * @see IPublisher#publish(String, String, Date, Date)
	 */
	public boolean publish(String providerName, String location, Date availableFrom, Date availableDate) {
		messageBus.publish(this, providerName, location, availableFrom, availableDate);
		return true;
	}

	/**
	 * @see IPublisher#getName()
	 */
	public String getName() {
		return this.name;
	}

}
