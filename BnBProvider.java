package AvailabilityDemand;
import java.util.*;
public class BnBProvider implements IPublisher {

	private String providerName;

	private Room room;
	private List<Room> booked = new ArrayList<Room>();

	public boolean publish(String providerName, String location, Date availableFrom, Date avaliableDate) {
		this.providerName = providerName;
		this.room = new Room(location, availableFrom, avaliableDate);

		Broker broke = getBroker();
		broke.attachPublisher(this);
		return true;
	}

	public Room getRoom(){
		return this.room;
	}

	public void addBookedRoom(Room toAdd){
		booked.add(toAdd);
	}

	public boolean removeBookedRoom(String location, Date start, Date end){
		boolean match = false;
		int len = booked.size();
		Room curr;

		for(int i = 0; i < len; i++)
		{
			curr = booked.get(i);
			match = curr.checkMatch(location, start, end);

			if(match){
				booked.remove(i);
				break;
			}
		}
		return match;
	}

	public boolean checkAvailability(Room custRoom){
		boolean valid = this.room.checkAvailability(custRoom);

		if(!valid)
			return valid;

		int len = booked.size();
		Room curr;

		for(int i = 0; i < len; i++)
		{
			curr = booked.get(i);
			valid = curr.checkIfValid(custRoom);

			if(!valid)
				break;
		}
		return valid;
	}

	public boolean checkIfValid(Room addRoom){
		return this.room.checkIfValid(addRoom);
	}

	public String providerName(){
		return this.providerName;
	}

	private Broker getBroker(){
		return Broker.getInstance();
	}
}
