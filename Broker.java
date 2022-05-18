package AvailabilityDemand;
import java.util.*;
import java.text.*;

public class Broker{
	private static Broker single = null;

	private List<BnBProvider> bnBs;
	private List<Customer> customers;
	private List<String> output;

	private Broker() {
		reset();
	}

	public static Broker getInstance() {
		if(single == null)
			single = new Broker();

		return single;
	}

	public void attachPublisher(BnBProvider bnb) {
		boolean valid = checkIfValid(bnb);

		if(valid){
			bnBs.add(bnb);
			notifySubscribers();
		}
	}

	public void attachSubscriber(Customer cust) {
		customers.add(cust);
		notifySubscribers();
	}

	public void detachSubscriber(String name, String location, Date start, Date end) {
		int ind = findSubscriber(name, location, start, end);

		if(ind == -1)
			return;

		List<String> booked = customers.get(ind).getBooked();
		int lenBooked = booked.size();
		int lenPubs = bnBs.size();

		String currName;
		BnBProvider currBnB;
		boolean removed = false;

		for(int i = 0; i < lenBooked; i++){
			currName = booked.get(i);
			for(int j = 0; j < lenPubs; j++){
				currBnB = bnBs.get(j);

				if(currName.equalsIgnoreCase(currBnB.providerName())){
					removed = currBnB.removeBookedRoom(location, start, end);

					if(removed)
						break;
		}	}	}

		customers.remove(ind);
	}

	public List<String> returnOutput(){
		return this.output;
	}

	public void reset() {
		bnBs = new ArrayList<BnBProvider>();
		customers = new ArrayList<Customer>();
		output = new ArrayList<String>();
	}

	private boolean checkIfValid(BnBProvider bnbToAdd){
		String addName = bnbToAdd.providerName();
		Room addRoom = bnbToAdd.getRoom();

		BnBProvider curBnB;
		Room curRoom;
		String currName;
		int length = bnBs.size();

		boolean valid = true;

		for(int i = 0; i < length; i++){
			curBnB = bnBs.get(i);
			currName = curBnB.providerName();

			if(currName.equalsIgnoreCase(addName)){
				curRoom = curBnB.getRoom();
				valid = curRoom.checkIfValid(addRoom);

				if(!valid)
					break;
		}	}

		return valid;
	}

	private int findSubscriber(String name, String location, Date start, Date end){
		int length = customers.size();
		Customer cus;
		boolean found;
		for(int i = 0; i < length; i++){
			cus = customers.get(i);
			found = cus.checkContents(name, location, start, end);

			if(found)
				return i;
		}
		return -1;
	}

	private void notifySubscribers(){
		int sizeBnb = bnBs.size();
		int sizeCus = customers.size();

		if(sizeBnb == 0 || sizeCus == 0)
			return;

		List<BnBProvider> bnBsNew = new ArrayList<BnBProvider>();

		BnBProvider bnb;
		Customer cust;

		Room bnbRoom;
		Room custRoom;

		String out;
		boolean found;

		for(int i = 0; i <  sizeBnb; i++){
			bnb = bnBs.get(i);
			bnbRoom = bnb.getRoom();
			out = "";
			found = false;

			for(int j = 0; j < sizeCus; j++){
				cust = customers.get(j);
				custRoom = cust.getRoom();

				if(bnb.checkAvailability(custRoom)){
					cust.notifyAvailability(bnb.providerName(), bnbRoom.startDate(), bnbRoom.endDate());
					bnb.addBookedRoom(custRoom);
					found = true;
	}	}	}	}

	public void notified(String toEnt){
		output.add(toEnt);
	}
}
