package AvailabilityDemand;
import java.util.*;

public interface IPublisher {

	public abstract boolean publish(String providerName, String location, Date availableFrom, Date avaliableDate);

}
