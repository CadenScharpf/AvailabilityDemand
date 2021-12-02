package AvailabilityDemand;

import java.util.Date;

public interface ISubscriber {

	public abstract boolean subscribe(String location, Date from, Date to);

	public abstract boolean unSubscribe(String location, Date from, Date to);

	public abstract String getName();

	public abstract void notify(String message);
}
