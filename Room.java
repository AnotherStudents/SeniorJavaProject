package AvailabilityDemand;
import java.util.*;
public class Room {

	private String location;
	private StayPeriod stayPeriod;

	public Room(String location, Date availableFrom, Date avaliableDate) {
		this.location = location;
		this.stayPeriod = new StayPeriod(availableFrom, avaliableDate);
	}

	public boolean checkContents(String location, Date from, Date to){
		if(!this.location.equalsIgnoreCase(location))
			return false;

		return this.stayPeriod.checkDates(from, to);
	}

	public boolean checkAvailability(Room room){
		if(!this.location.equalsIgnoreCase(room.location))
			return false;

		return this.stayPeriod.checkAvailability(room.stayPeriod);
	}

	public boolean checkIfValid(Room room){
		if(!this.location.equalsIgnoreCase(room.location))
			return true;

		return this.stayPeriod.checkIfValid(room.stayPeriod);
	}

	public boolean checkMatch(String location, Date start, Date end){
		if(!this.location.equalsIgnoreCase(location))
			return false;

		return this.stayPeriod.checkMatch(start, end);
	}

	public String getLocation(){
		return this.location;
	}

	public Date startDate(){
		return this.stayPeriod.startDate();
	}

	public Date endDate(){
		return this.stayPeriod.endDate();
	}
}
