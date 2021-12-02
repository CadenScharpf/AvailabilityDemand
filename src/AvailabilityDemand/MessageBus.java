package AvailabilityDemand;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

class Subscription
{
	ISubscriber subscriber;
	StayPeriod stayPeriod;
	ArrayList<String> notifications;
	String name;

	public Subscription(ISubscriber subscriber, StayPeriod stayPeriod)
	{
		this.subscriber = subscriber;
		this.stayPeriod = stayPeriod;
		this.name = subscriber.getName();
		this.notifications = new ArrayList<String>();
	}
	boolean isSubsetOf(StayPeriod stayPeriod)
	{return this.stayPeriod.isSubsetOf(stayPeriod);}
	void notify(String name, String location, StayPeriod stayPeriod)
	{
		String message = getMessageString(name, location, stayPeriod);
		if(notifications.contains(message)){return;}
		this.notifications.add(message);
		subscriber.notify(message);
	}
	String getMessageString(String name, String loc, StayPeriod stayPeriod)
	{
		SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
		String startDateStr = f.format(stayPeriod.startDate);
		String endDateStr = f.format(stayPeriod.endDate);
		return name+","+loc+","+startDateStr+","+endDateStr;
	}
}
public class MessageBus {

	//subscriberPool: Location -> List of subscribers seeking that city
	private HashMap<String, List<Subscription>> subscriberPool;
	//roomPool: Location -> IPublisher -> Room
	private HashMap<String,HashMap<IPublisher,Room>> roomPool;

	ArrayList<String> counts;

	public MessageBus()
	{
		subscriberPool = new HashMap<String, List<Subscription>>();
		roomPool = new HashMap<String,HashMap<IPublisher,Room>>();
		this.counts = new ArrayList<String>();
	}

	public boolean publish(IPublisher publisher, String providerName, String location, Date availableFrom, Date availableDate) {
		if(!roomPool.containsKey(location)){roomPool.put(location, new HashMap<IPublisher,Room>());}
		HashMap<IPublisher,Room> locRooms = roomPool.get(location);

		//init publishers room for this city if !exists
		if(!locRooms.containsKey(publisher))
		{locRooms.put(publisher, new Room(publisher, location));}
		Room room = locRooms.get(publisher);

		//check for overlap
		StayPeriod publishedPeriod = new StayPeriod(availableFrom, availableDate);
		int result = overlapSearch(room, publishedPeriod);//TODO
		if(result>0){return false;}

		//insert to room pool
		room.stayPeriods.add(publishedPeriod);
		locRooms.put(publisher, room);

		//check existing subscribers
		ArrayList<Subscription> subs = getMatchingSubscriptions(publishedPeriod, location);
		for(Subscription sub : subs){sub.notify(providerName, location, publishedPeriod);}
		return true;
	}

	public boolean subscribe(ISubscriber subscriber, String name, String location, Date startDate, Date endDate) {
		String subscriptionStr = name + location + startDate.getTime() + endDate.getTime();
		if(counts.contains(subscriptionStr)){return false;}
		counts.add(subscriptionStr);
		StayPeriod stayPeriod = new StayPeriod(startDate, endDate);
		Subscription sub = new Subscription(subscriber, stayPeriod);
		//init list for location if not initialized
		if(!subscriberPool.containsKey(location)) {subscriberPool.put(location,new ArrayList<Subscription>());}
		//register sub to the pool
		subscriberPool.get(location).add(sub);
		//search for existing rooms
		List<Reservable> matches = getMatchingRooms(location, stayPeriod);
		//send notifications
		if(matches == null){return true;}
		for(Reservable r : matches){sub.notify(r.name, r.location, new StayPeriod(r.startDate, r.endDate));}
		return true;
	}

	public void unsubscribe(ISubscriber subscriber, String name, String location, Date startDate, Date endDate)
	{
		Subscription sub = deleteSubscription(name, location, startDate, endDate);
	}

	public Subscription deleteSubscription(String name, String location, Date startDate, Date endDate)
	{
		List<Subscription> locationSubs = subscriberPool.get(location);
		for(int i = 0; i < locationSubs.size(); i++)
		{
			Subscription sub = locationSubs.get(i);
			if(sub.name == name && sub.stayPeriod.startDate == startDate && sub.stayPeriod.endDate == endDate)
			{ return locationSubs.remove(i);}
		}
		return null;
	};

	public List<Reservable> getMatchingRooms(String location, StayPeriod stayPeriod)
	{
		List<Reservable> matches = new ArrayList<Reservable>();
		HashMap<IPublisher,Room> locListings = roomPool.get(location);
		if(locListings == null){return matches;}
		for(IPublisher publisher : locListings.keySet())
		{
			Room room = locListings.get(publisher);
			Reservable match = room.availableFor(stayPeriod);

			if (match != null) {
				if (matches.size() == 0) {
					matches.add(match);
				} else {
					int i=0;
					for (; i < matches.size(); i++) {
						if (matches.get(i).created > match.created)
						{matches.add(i,match);}
					}
					if(i>=matches.size()){matches.add(match);}

				}

			}
		}
		ArrayList<Reservable> ret = new ArrayList<Reservable>();
		for(int i = 0; i < matches.size(); i++)
		{
			ret.add(matches.get(matches.size()-1-i));
		}
		return (matches.size() == 0) ? null:ret;
	}

	public ArrayList<Subscription> getMatchingSubscriptions(StayPeriod publishedPeriod, String location)
	{
		ArrayList<Subscription> subs = new ArrayList<Subscription>();
		if(!subscriberPool.containsKey(location)){return subs;}
		for(Subscription sub: subscriberPool.get(location))
		{
			if(publishedPeriod.isSupersetOf(sub.stayPeriod))
			{
				subs.add(sub);
			}
		}
		return subs;
	}

	private int overlapSearch(Room room, StayPeriod stayPeriod) {
		int i = 0;
		for(; i < room.stayPeriods.size(); i++)
		{
			StayPeriod sp = room.stayPeriods.get(i);
			if(stayPeriod.overlaps(sp)){return i;}
		}
		return -1;
	}

}
