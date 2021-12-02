package AvailabilityDemand;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

class Reservable extends StayPeriod
{
	public IPublisher publisher;
	public String name;
	public String location;

	public Reservable(IPublisher publisher, String name, String location, StayPeriod availabilityPeriod) {
		super(availabilityPeriod.startDate, availabilityPeriod.endDate);
		this.publisher = publisher;
		this.name = name;
		this.location = location;
	}
}
public class Room {
	IPublisher publisher;
	private String location;
	public ArrayList<StayPeriod> stayPeriods;
	public Room(IPublisher publisher, String location)
	{
		this.publisher = publisher;
		this.location = location;
		this.stayPeriods = new ArrayList<StayPeriod>();
	}

	 public Reservable availableFor(StayPeriod stayPeriod)
	{
		for(StayPeriod availabilityPeriod : stayPeriods)
		{
			if(stayPeriod.isSubsetOf(availabilityPeriod))
			{ return new Reservable(this.publisher, this.publisher.getName(), this.location, availabilityPeriod);}
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Room room = (Room) o;
		return publisher.equals(room.publisher) && location.equals(room.location) && stayPeriods.equals(room.stayPeriods);
	}

	@Override
	public int hashCode() {
		return Objects.hash(publisher, location, stayPeriods);
	}
}
