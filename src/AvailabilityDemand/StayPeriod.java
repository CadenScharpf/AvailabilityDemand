package AvailabilityDemand;

import java.util.Date;

public class StayPeriod {

	public Date startDate;
	public Date endDate;
	public long created;
	public static long count = 0;


	public StayPeriod(Date startDate, Date endDate)
	{
		this.startDate = startDate;
		this.endDate = endDate;
		created = count;
		count++;
	}

	/*
	*Returns true if the StayPeriod argument fully encapsulates
	*the date range of the object being called
	 */
	public boolean isSubsetOf(StayPeriod stayPeriod)
	{
		if
		(
				this.startDate.getTime() >= stayPeriod.startDate.getTime() &&
				this.endDate.getTime() <= stayPeriod.endDate.getTime()
		){ return true;}
		return false;
	}
	public boolean isSupersetOf(StayPeriod stayPeriod)
	{
		if(this.startDate.getTime() <= stayPeriod.startDate.getTime() &&
				this.endDate.getTime() >= stayPeriod.endDate.getTime()){return  true;}
		return false;
	}

	public boolean overlaps(StayPeriod stayPeriod)
	{
		if(
				((this.startDate.getTime()<stayPeriod.endDate.getTime())&&(this.endDate.getTime() > endDate.getTime()))||
				((this.startDate.getTime()<stayPeriod.startDate.getTime() && (this.endDate.getTime() > stayPeriod.startDate.getTime())))
		)
		{return true;}
		return false;
	}
}
