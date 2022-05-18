package AvailabilityDemand;
import java.util.*;
import java.text.*;

public class AvailabilityDemand {

	private static AvailabilityDemand single = null;

	private AvailabilityDemand() {
		Broker broke = Broker.getInstance();
		broke.reset();
	}

	public void processInput(String command) {
		String[] parts = command.split(",");

		if(parts.length != 5)
			return;

		String prompt = parts[0].trim().replaceAll("\\s{2,}", " ");
		String name = parts[1].trim().replaceAll("\\s{2,}", " ");
		String location = parts[2].trim().replaceAll("\\s{2,}", " ");

		Date start;
		Date end;
		Date defaultDate;
		SimpleDateFormat convert = new SimpleDateFormat("MM/dd/yyyy");

		try{
			start = convert.parse(parts[3]);
			end = convert.parse(parts[4]);
			defaultDate = convert.parse("11/27/2021");
		}catch(Throwable e){
			return;
		}

		if(start.compareTo(end) > 0 || start.compareTo(defaultDate) <= 0)
			return;

		if(prompt.equalsIgnoreCase("publish"))
			createPublisher(name, location, start, end);
		else if(prompt.equalsIgnoreCase("subscribe"))
			createSubscriber(name, location, start, end);
		else if(prompt.equalsIgnoreCase("unSubscribe"))
			removeSubscriber(name, location, start, end);
	}

	private void createPublisher(String name, String location, Date start, Date end) {
		BnBProvider bnb = new BnBProvider();
		bnb.publish(name, location, start, end);
	}

	private void createSubscriber(String name, String location, Date start, Date end) {
		Customer cus = new Customer(name);
		cus.subscribe(location, start, end);
	}

	private void removeSubscriber(String name, String location, Date start, Date end) {
		Customer cus = new Customer(name);
		cus.unSubscribe(location, start, end);
	}

	public List<String> getAggregatedOutput() {
		Broker broke = Broker.getInstance();
		return broke.returnOutput();
	}

	public void reset() {
		Broker broke = Broker.getInstance();
		broke.reset();
	}

	/*public static void main(String[] args) {
		AvailabilityDemand ava = new AvailabilityDemand();
		List<String> out;

		//Test 1 - Correct
		ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		ava.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/15/2021");
		ava.processInput("subscribe, Jane Doe, Tempe, 11/29/2021, 12/02/2021");
		ava.processInput("publish, High-Mountains, Tempe, 11/28/2021, 12/02/2021");
		ava.processInput("publish, AirCloud, Tempe, 11/28/2021, 12/05/2021");
		out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));

		for(int i = 0; i< out.size(); i++) {
			System.out.println(out.get(i));
		}


		ava.reset();
		System.out.println("\n\nNEW TEST \n");


		//Test 2 - Correct
		ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		ava.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
		// John Doe's subscription fits within the criteria thus, John Doe should get notified of this event
		ava.processInput("publish, High-Mountains, New York City, 12/01/2021, 12/05/2021");
		// William's subscription fits within the criteria thus, William should getnotified of this event
		ava.processInput("publish, AirClouds, New York City, 12/10/2021, 12/15/2021");
		out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));
		for(int i = 0; i< out.size(); i++) {
			System.out.println(out.get(i));
		}

		ava.reset();
		System.out.println("\n\nNEW TEST \n");


		//Test 3 - Correct
		//Date format invalid: DD/MM/YYYY
		ava.processInput("subscribe, John Doe, New York City, 15/01/2022, 30/01/2022");
		//Date format invalid: DD MMM YYYY
		ava.processInput("publish, High-Mountains, New York City, 14Jan 2022, 30 Jan 2022");
		//stay period to date must be smaller than stay period from date
		ava.processInput("subscribe, John Doe, New York City, 30/01/2022, 15/01/2022");
		//extra parameter in the publish method
		ava.processInput("publish, High-Mountains, New York City, 14Jan 2022, 30 Jan 2022, great view and lot of space");
		//available till date must be smaller than available from date
		ava.processInput("publish, AirClouds, New York City, 30/01/2022, 15/01/2022");

		out = ava.getAggregatedOutput();
		System.out.println(out.size());
		//System.out.println(Arrays.toString(out.toArray()));


		ava.reset();
		System.out.println("\n\nNEW TEST \n");



		//Test 4 - checks array size, should be 0 - Correct
		ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		ava.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
		// to date needs to be same as or greater than end date of subscribed period
		ava.processInput("publish, High-Mountains, New York City, 11/29/2021, 12/02/2021");
		// start date of availability period needs to be later than default date which is 11/27/2021
		ava.processInput("publish, High-Mountains, New York City, 11/20/2021, 12/05/2021");
		// start date of stay period needs to be later than default date which is 11/27/2021
		ava.processInput("subscribe, Jane Doe, New York City, 11/20/2021, 12/05/2021");

		out = ava.getAggregatedOutput();
		System.out.println(out.size());


		ava.reset();
		System.out.println("\n\nNEW TEST \n");


		//Test 5 -- Correct
		ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		ava.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/05/2021");
		//overlaps with the first published availability, should be discarded
		ava.processInput("publish, High-Mountains, New York City, 11/29/2021, 12/02/2021");
		//overlaps with the first published availability, should be discarded
		ava.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/15/2021");

		out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));
		for(int i = 0; i< out.size(); i++) {
			System.out.println(out.get(i));
		}

		ava.reset();
		System.out.println("\n\nNEW TEST \n");


		//Test 6 -- Correct
		//			Events should be stored so new subscribers can also see them.
		ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		ava.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
		//both subscribers should get the notification as satisfy the criteria
		ava.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/15/2021");
		//one subscriber removed from system
		ava.processInput("unsubscribe, William, New York City, 12/10/2021, 12/15/2021");
		//duplicate published event, no action taken
		ava.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/15/2021");
		//one subscriber removed from system, no subscribers in system
		ava.processInput("unsubscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		//no subscribers in system, system will store the event
		ava.processInput("publish, AirClouds, New York City, 11/30/2021, 12/15/2021");
		// both stored published events will be fired for below customer since thisone comes as a new subscription
		ava.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
		// both stored published events will be fired for below customer since thisone comes as a new subscription
		ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));
		for(int i = 0; i< out.size(); i++) {
			System.out.println(out.get(i));
		}

		//ava.processInput("subscribe, Jane Doe, Atlanta   , 12/01/2021, 12/05/2021");
		//ava.processInput("publish, High-Mountains, New      York City, 11/30/2021, 12/15/2021");
		//ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");

		//out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));
		//ava.processInput("unsubscribe, Jane Doe, New York City, 12/01/2021, 12/05/2021");
		//ava.processInput("publish, High-Mountains, Atlanta, 11/30/2021, 12/15/2021");

		//out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));
		//ava.reset();

		//ava.processInput("subscribe, Jane Doe, New York City, 12/01/2021, 12/05/2021");
		//ava.processInput("publish, High-Mountains, New      York City, 11/30/2021, 12/15/2021");
		//ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		//ava.processInput("unsubscribe, Jane Doe, New York City, 12/01/2021, 12/05/2021");
		//out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));
	}*/

}


