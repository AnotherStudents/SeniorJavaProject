package AvailabilityDemand;
import java.util.*;
public class StayPeriod {

	private Date startDate;
	private Date endDate;

	public StayPeriod(Date availableFrom, Date avaliableDate) {
		this.startDate = availableFrom;
		this.endDate = avaliableDate;
	}

	public boolean checkDates(Date from, Date to){
		boolean toRet = false;
		if(this.startDate.compareTo(from) == 0 && this.endDate.compareTo(to) == 0)
			toRet = true;

		return toRet;
	}

	public boolean checkAvailability(StayPeriod avaliable){
		boolean toRet = false;
		if(this.startDate.compareTo(avaliable.startDate) <= 0 && this.endDate.compareTo(avaliable.endDate) >= 0)
			toRet = true;

		return toRet;
	}

	public boolean checkIfValid(StayPeriod time){
		boolean toRet = false;
		if(this.startDate.compareTo(time.startDate) < 0 && this.endDate.compareTo(time.startDate) < 0)
			toRet = true;
		else if(this.startDate.compareTo(time.endDate) > 0 && this.endDate.compareTo(time.endDate) > 0)
			toRet = true;

		return toRet;
	}

	public boolean checkMatch(Date start, Date end){
		boolean toRet = false;
		if(this.startDate.compareTo(start) == 0 && this.endDate.compareTo(end) == 0)
			toRet = true;
		return toRet;
	}

	public Date startDate(){
		return this.startDate;
	}

	public Date endDate(){
		return this.endDate;
	}
}
