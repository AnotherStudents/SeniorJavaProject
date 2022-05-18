package AvailabilityDemand;
import java.util.*;
import java.text.*;

public class Customer implements ISubscriber {

	private String name;
	private Room room;
	private List<String> booked;


	public Customer(String name){
		this.name = name;
	}

	public boolean subscribe(String location, Date from, Date to) {
		this.room = new Room(location, from, to);

		Broker broke = getBroker();
		broke.attachSubscriber(this);
		return true;
	}

	public boolean unSubscribe(String location, Date from, Date to) {
		this.room = new Room(location, from, to);

		Broker broke = getBroker();
		broke.detachSubscriber(this.name, location, from, to);
		return true;
	}

	public void notifyAvailability(String bbName, Date from, Date to){
		SimpleDateFormat convert = new SimpleDateFormat("MM/dd/yyyy");

		String response = this.name + " notified of B&B availability in " +
			this.room.getLocation() + " from " +
			convert.format(from) + " to " +
			convert.format(to) + " by " +
			bbName + " B&B";

		booked.add(bbName);
		Broker broke = getBroker();
		broke.notified(response);
	}

	private Broker getBroker(){
		return Broker.getInstance();
	}

	public boolean checkContents(String name, String location, Date from, Date to){
		if(!this.name.equalsIgnoreCase(name))
			return false;

		return this.room.checkContents(location, from, to);
	}

	public Room getRoom(){
		return this.room;
	}

	public String getName(){
		return this.name;
	}

	public List<String> getBooked(){
		return this.booked;
	}
}
